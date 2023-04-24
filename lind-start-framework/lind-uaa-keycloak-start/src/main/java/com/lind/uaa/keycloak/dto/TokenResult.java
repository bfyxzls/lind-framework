package com.lind.uaa.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenResult {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("id_token")
	private String idToken;

	@JsonProperty("session_state")
	private String sessionState;

	private String scope;

	@JsonProperty("expires_in")
	private long expiresIn;

	@JsonProperty("refresh_expires_in")
	private long refreshExpiresIn;

	@JsonProperty("not-before-policy")
	private long notBeforePolicy;

}
