package com.lind.uaa.jwt.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 登出的事件.
 */
@Data
@AllArgsConstructor
public class LogoutSuccessEvent {

	private UserDetails userDetails;

	private String ipAddress;

}
