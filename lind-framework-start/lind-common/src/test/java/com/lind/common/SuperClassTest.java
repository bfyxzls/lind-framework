package com.lind.common;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 类型的协变和逆变 逆变与协变用来描述类型转换（type
 * transformation）后的继承关系，其定义：如果A、B表示类型，f(⋅)表示类型转换，≤表示继承关系（比如，A≤B表示A是由B派生出来的子类）；
 * f(⋅)是逆变（contravariant）的，当A≤B时有f(B)≤f(A)成立； f(⋅)是协变（covariant）的，当A≤B时有f(A)≤f(B)成立； f(⋅)
 * 是不变（invariant）的，当A≤B时上述两个式子均不成立，即f(A)与f(B)相互之间没有继承关系。
 */
@Slf4j
public class SuperClassTest {

	void print(Super super1) {
		log.info("{}", super1);
	}

	@Test
	public void superTest() {
		Sub sub = new Sub();
		sub.setName("zzl");
		sub.setEmail("zz@sina.com");
		print(sub);
	}

	@Test
	public void extendsParam() {
		ShopIdentityProviderConfig shopIdentityProviderConfig = new ShopIdentityProviderConfig();
		shopIdentityProviderConfig.setTitle("demo");
		shopIdentityProviderConfig.setInfo("描述");
		AbstractOAuth2IdentityProvider<ShopIdentityProviderConfig> abstractOAuth2IdentityProvider = new ShopAbstractOAuth2IdentityProvider(
				shopIdentityProviderConfig);
	}

	@Data
	class Super {

		String name;

	}

	@Data
	@ToString(callSuper = true)
	class Sub extends Super {

		String email;

	}

	/**
	 * 超级配置
	 */
	@Data
	public class OAuth2IdentityProviderConfig {

		private String title;

	}

	/**
	 * 具体配置
	 */
	@Data
	@ToString(callSuper = true)
	public class ShopIdentityProviderConfig extends OAuth2IdentityProviderConfig {

		private String info;

	}

	/**
	 * 基本实现
	 *
	 * @param <C>
	 */
	public abstract class AbstractOAuth2IdentityProvider<C extends OAuth2IdentityProviderConfig> {

		C config;

		public AbstractOAuth2IdentityProvider(C config) {
			System.out.println(config.toString());
			this.config = config;
		}

		public C getConfig() {
			return this.config;
		}

	}

	/**
	 * 具体实现
	 **/
	public class ShopAbstractOAuth2IdentityProvider extends AbstractOAuth2IdentityProvider<ShopIdentityProviderConfig> {

		public ShopAbstractOAuth2IdentityProvider(ShopIdentityProviderConfig config) {
			super(config);
			System.out.println(config.toString());
		}

	}

}
