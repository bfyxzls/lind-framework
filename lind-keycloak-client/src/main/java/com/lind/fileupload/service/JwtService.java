package com.lind.fileupload.service;

import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     com.edw.service.JwtService
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 20 Agt 2020 15:17
 */
@Service
public class JwtService {

    @Value("${kc.jwk-set-uri}")
    private String jwksUrl;

    @Value("${kc.certs-id}")
    private String certsId;

    @Cacheable(value = "jwkCache")
    public Jwk getJwk() throws Exception {
        return new UrlJwkProvider(new URL(jwksUrl)).get(certsId);
    }
}
