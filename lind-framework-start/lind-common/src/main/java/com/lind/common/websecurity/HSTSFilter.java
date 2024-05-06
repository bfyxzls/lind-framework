package com.lind.common.websecurity;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lind
 * @date 2024/5/6 10:03
 * @since 1.0.0
 */
public class HSTSFilter extends OncePerRequestFilter {

	private static final String SECURITY_POLICY_HEADER = "Strict-Transport-Security";

	private static final String SECURITY_POLICY = "max-age=31536000";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		response.setHeader(SECURITY_POLICY_HEADER, SECURITY_POLICY);
		filterChain.doFilter(request, response);
	}

}
