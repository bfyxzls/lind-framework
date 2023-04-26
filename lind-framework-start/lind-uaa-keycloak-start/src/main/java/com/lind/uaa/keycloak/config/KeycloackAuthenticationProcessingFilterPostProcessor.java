package com.lind.uaa.keycloak.config;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.AdapterTokenStore;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.OAuthRequestAuthenticator;
import org.keycloak.adapters.RequestAuthenticator;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.authentication.SpringSecurityRequestAuthenticator;
import org.keycloak.adapters.springsecurity.authentication.SpringSecurityRequestAuthenticatorFactory;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.servlet.http.HttpServletRequest;

import static com.lind.uaa.keycloak.config.Constant.TOKEN_AUTHORIZATION_CODE_REDIRECT;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_AUTHORIZATION_CODE_RESPONSE;
import static com.lind.uaa.keycloak.config.Constant.getCurrentHost;

/**
 * 自定义的回调地址,复盖默认的sso/login.
 */
public class KeycloackAuthenticationProcessingFilterPostProcessor implements BeanPostProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(KeycloackAuthenticationProcessingFilterPostProcessor.class);

	@Autowired
	UaaProperties uaaProperties;

	private void process(KeycloakAuthenticationProcessingFilter filter) {
		System.out.println("KeycloakAuthenticationProcessingFilter authenticate");

		filter.setRequestAuthenticatorFactory(new SpringSecurityRequestAuthenticatorFactory() {
			@Override
			public RequestAuthenticator createRequestAuthenticator(HttpFacade facade, HttpServletRequest request,
					KeycloakDeployment deployment, AdapterTokenStore tokenStore, int sslRedirectPort) {
				return new SpringSecurityRequestAuthenticator(facade, request, deployment, tokenStore,
						sslRedirectPort) {

					@Override
					protected OAuthRequestAuthenticator createOAuthAuthenticator() {
						return new OAuthRequestAuthenticator(this, facade, deployment, sslRedirectPort, tokenStore) {
							@Override
							protected String getRequestUrl() {
								return StringUtils.isNotBlank(uaaProperties.getRedirectUri())
										? getCurrentHost(request) + TOKEN_AUTHORIZATION_CODE_REDIRECT
										: getCurrentHost(request) + TOKEN_AUTHORIZATION_CODE_RESPONSE;
							}
						};
					}

				};
			}
		});
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof KeycloakAuthenticationProcessingFilter) {
			logger.info("Injecting core KeycloakAuthenticationProcessingFilter handler...");
			process(((KeycloakAuthenticationProcessingFilter) bean));
		}
		return bean;
	}

}