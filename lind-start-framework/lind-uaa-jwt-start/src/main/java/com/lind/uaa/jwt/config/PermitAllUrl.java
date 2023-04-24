package com.lind.uaa.jwt.config;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 需要放开权限的url
 */
public final class PermitAllUrl {

	static final Logger logger = LoggerFactory.getLogger(PermitAllUrl.class);

	/**
	 * 监控中心和swagger需要访问的url
	 */
	static final List<String> ENDPOINTS = ImmutableList.of("/actuator/**", "/v2/api-docs/**", "/doc.html",
			"/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/oauth/token", "/oauth/test", "/token/**",
			"/login", "/view/**", "/deployByFile*", "/modeler.html*", "/editor-app/**", "/model/**", "/deployment/**",
			"/task/**", "/execution/**", "/editor/**", "/logo,png", "/refresh", "/test-verify");

	/**
	 * 需要放开权限的url
	 * @param urls 自定义的url
	 * @return 自定义的url和监控中心需要访问的url集合
	 */
	public static String[] permitAllUrl(String... urls) {
		if (urls == null || urls.length == 0 || StringUtils.isAllBlank(urls)) {
			return ENDPOINTS.toArray(new String[ENDPOINTS.size()]);
		}

		Set<String> set = new HashSet<>();
		set.addAll(ENDPOINTS);
		Collections.addAll(set, urls);
		logger.info("permit urls:{}", set);
		return set.toArray(new String[set.size()]);
	}

}
