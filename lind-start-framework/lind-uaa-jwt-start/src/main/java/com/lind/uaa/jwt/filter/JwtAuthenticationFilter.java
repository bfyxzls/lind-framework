package com.lind.uaa.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.JwtAuthenticationToken;
import com.lind.uaa.jwt.entity.ResourceUser;
import com.lind.uaa.jwt.utils.UserContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lind.uaa.jwt.config.Constants.ONLINE_USER;

/**
 * 从请求中提取Header-Authorization中的jwtToken进行校验.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final RequestMatcher requiresAuthenticationRequestMatcher;

	@Autowired
	RedisService redisService;

	private List<RequestMatcher> permissiveRequestMatchers;

	private AuthenticationManager authenticationManager;

	private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

	private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

	public JwtAuthenticationFilter() {
		// 拦截header中带Authorization的请求
		this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(authenticationManager, "authenticationManager must be specified");
		Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
		Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
	}

	protected String getJwtToken(HttpServletRequest request) {
		String authInfo = request.getHeader("Authorization");
		authInfo = StringUtils.removeStart(authInfo, "bearer ");
		return StringUtils.removeStart(authInfo, "Bearer ");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// header没带token的,直接放行.
		if (!requiresAuthentication(request, response)) {
			filterChain.doFilter(request, response);
			return;
		}
		Authentication authResult = null;
		AuthenticationException failed = null;
		DecodedJWT decodedJWT = null;
		try {
			String token = getJwtToken(request);
			if (StringUtils.isNotBlank(token)) {
				decodedJWT = JWT.decode(token);
				JwtAuthenticationToken authToken = new JwtAuthenticationToken(decodedJWT);
				// jwt自身有效期和签名校验
				authResult = this.getAuthenticationManager().authenticate(authToken);
				// redis实时校验，如果用户手动单击登录，也将失败
				if (!redisService.hasKey(ONLINE_USER + token)) {
					failed = new InsufficientAuthenticationException("用户已经退出");
				}
			}
			else {
				failed = new InsufficientAuthenticationException("JWT is Empty");
			}
		}
		catch (JWTDecodeException e) {
			logger.error("JWT format error", e);
			failed = new InsufficientAuthenticationException("JWT format error", failed);
		}
		catch (InternalAuthenticationServiceException e) {
			logger.error("An internal error occurred while trying to authenticate the user.", failed);
			failed = e;
		}
		catch (AuthenticationException e) {
			// Authentication failed
			failed = e;
		}
		catch (Exception e) {
			// Authentication failed
			logger.error(e);
		}
		if (authResult != null && failed == null) {
			ResourceUser userDetails = (ResourceUser) authResult.getPrincipal();
			UserContextHolder.setUserName(userDetails.getUsername());
			UserContextHolder.setUserId(userDetails.getId());
			UserContextHolder.setUser(userDetails);
			successfulAuthentication(request, response, filterChain, authResult);
		}
		else if (!permissiveRequest(request)) {
			unsuccessfulAuthentication(request, response, failed);
			return;
		}

		filterChain.doFilter(request, response);
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return requiresAuthenticationRequestMatcher.matches(request);
	}

	protected boolean permissiveRequest(HttpServletRequest request) {
		if (permissiveRequestMatchers == null)
			return false;
		for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
			if (permissiveMatcher.matches(request))
				return true;
		}
		return false;
	}

	public void setPermissiveUrl(String... urls) {
		if (permissiveRequestMatchers == null)
			permissiveRequestMatchers = new ArrayList<>();
		for (String url : urls)
			permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
	}

	public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
		Assert.notNull(successHandler, "successHandler cannot be null");
		this.successHandler = successHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
		Assert.notNull(failureHandler, "failureHandler cannot be null");
		this.failureHandler = failureHandler;
	}

	protected AuthenticationSuccessHandler getSuccessHandler() {
		return successHandler;
	}

	protected AuthenticationFailureHandler getFailureHandler() {
		return failureHandler;
	}

}
