package com.lind.uaa.dto;

import lombok.Data;

@Data
public class LoginParam {

	private String username;

	private String password;

	private String loginType;

	private String grantType = "password";

	private String clientId = "system";

	private String scope = "app";

	private String clientSecret = "system";

}
