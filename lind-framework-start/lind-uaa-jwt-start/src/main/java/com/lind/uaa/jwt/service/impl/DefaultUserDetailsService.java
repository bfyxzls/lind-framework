package com.lind.uaa.jwt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 这个是为非授权服务提供的默认实现，不做任务事.
 */
@Slf4j
public class DefaultUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		return null;
	}

}
