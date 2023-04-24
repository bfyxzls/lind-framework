package com.lind.uaa.jwt.utils;

import lombok.Data;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Data
public class RSA256Key {

	private RSAPublicKey publicKey;

	private RSAPrivateKey privateKey;

}
