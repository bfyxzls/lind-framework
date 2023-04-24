package com.lind.uaa.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.JwtConfig;
import com.lind.uaa.jwt.entity.ResourceRole;
import com.lind.uaa.jwt.entity.ResourceUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * jwt操作类.
 */
@Component
public class JwtUserService {

	public static final String CLAIM = "roles";

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	JwtConfig jwtConfig;

	@Autowired
	RedisService redisService;

	@Autowired
	HttpServletRequest request;

	/**
	 * 3.登陆成功后，存储这个用户信息.
	 * @param userDetails
	 * @return
	 */
	@SneakyThrows
	public String generateJwtJoinUser(ResourceUser userDetails) {
		Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecret());
		Date date = DateUtils.addMinutes(new Date(), Integer.parseInt(jwtConfig.getExpiresAt().toString()));
		JWTCreator.Builder jwt = JWT.create().withIssuedAt(new Date()).withIssuer(getIssue())
				.withJWTId(UUID.randomUUID().toString()).withKeyId(userDetails.getId())
				.withSubject(userDetails.getUsername())
				.withArrayClaim(CLAIM, userDetails.getAuthorities().stream().map(o -> o.toString())
						.collect(Collectors.toList()).toArray(new String[] {}))
				.withExpiresAt(date).withIssuedAt(new Date());
		return jwt.sign(algorithm);
	}

	public ResourceUser getUserByJwt(DecodedJWT jwt) {
		ResourceUser resourceUser = new ResourceUser() {
			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public String getUsername() {
				return null;
			}

			@Override
			public String getEmail() {
				return null;
			}

			@Override
			public String getId() {
				return jwt.getKeyId();
			}

			@Override
			public List<? extends ResourceRole> getResourceRoles() {

				List<ResourceRole> resourceRoles = new ArrayList<>();
				if (jwt.getClaim(CLAIM) != null) {
					for (String role : jwt.getClaim(CLAIM).asArray(String.class)) {
						resourceRoles.add(new ResourceRole() {
							@Override
							public String getId() {
								return null;
							}

							@Override
							public String getName() {
								return role;
							}
						});
					}
				}
				return resourceRoles;
			}

			@Override
			public void setResourceRoles(List<? extends ResourceRole> resourceRoles) {

			}
		};
		return resourceUser;
	}

	private String getIssue() {
		String uri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		return uri;
	}

	/**
	 * 通过token解析用户信息.
	 * @param token
	 * @return
	 */
	@SneakyThrows
	public ResourceUser getUserDetailsByToken(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return getUserByJwt(jwt);
	}

}
