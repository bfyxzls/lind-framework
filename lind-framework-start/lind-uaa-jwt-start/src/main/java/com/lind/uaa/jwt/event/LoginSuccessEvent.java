package com.lind.uaa.jwt.event;

import com.lind.uaa.jwt.entity.TokenResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录成功的事件.
 */
@Data
@AllArgsConstructor
public class LoginSuccessEvent {

	private TokenResult tokenResult;

	private String ipAddress;

}
