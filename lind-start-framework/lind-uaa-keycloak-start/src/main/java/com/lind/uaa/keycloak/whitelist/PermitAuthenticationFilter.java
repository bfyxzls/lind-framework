package com.lind.uaa.keycloak.whitelist;

import com.lind.uaa.keycloak.cache.CacheProvider;
import com.lind.uaa.keycloak.config.Constant;
import com.lind.uaa.keycloak.config.UaaProperties;
import com.lind.uaa.keycloak.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import static com.lind.uaa.keycloak.config.Constant.AUTHORIZATION;

/**
 * 白名单过滤器，完成将header中的Authorization删除.
 */
@Component("permitAuthenticationFilter")
@RequiredArgsConstructor
@Slf4j
public class PermitAuthenticationFilter extends OncePerRequestFilter {

	private final UaaProperties uaaProperties;

	@Autowired
	CacheProvider cacheProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Set<String> set = new HashSet<>();
		String[] urls = uaaProperties.getPermitAll();
		if (urls != null && urls.length > 0) {
			Collections.addAll(set, urls);
		}
		if (set.contains(request.getRequestURI()) || set.contains(request.getServletPath())) {

			request = new HttpServletRequestWrapper(request) {
				private Set<String> headerNameSet;

				@Override
				public Enumeration<String> getHeaderNames() {
					if (headerNameSet == null) {
						headerNameSet = new HashSet<>();
						Enumeration<String> wrappedHeaderNames = super.getHeaderNames();
						while (wrappedHeaderNames.hasMoreElements()) {
							String headerName = wrappedHeaderNames.nextElement();
							if (!AUTHORIZATION.equalsIgnoreCase(headerName)) {
								headerNameSet.add(headerName);
							}
						}
					}
					return Collections.enumeration(headerNameSet);
				}

				@Override
				public Enumeration<String> getHeaders(String name) {
					return super.getHeaders(name);
				}

				@Override
				public String getHeader(String name) {
					return super.getHeader(name);
				}
			};

		}
		String token = request.getHeader(AUTHORIZATION);
		if (token != null) {
			String userId = TokenUtil.getSubject(token);
			String key = TokenUtil.NEED_REFRESH_TOKEN + ":" + userId;
			if (cacheProvider.hasKey(key)) {
				response.addHeader(TokenUtil.NEED_REFRESH_TOKEN, "1");
				cacheProvider.delete(key);
			}
		}
		filterChain.doFilter(request, response);
	}

}
