package com.lind.uaa.jwt;

import com.lind.uaa.jwt.utils.JwtUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class JwtUtilsTest {

	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/*
	 * 将时间转换为时间戳
	 */
	@SneakyThrows
	public static String dateToStamp(String s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	@SneakyThrows
	@Test
	public void create() {
		String token = JwtUtils.generTokenByRS256(new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}

			@Override
			public String getPassword() {
				return "abc123";
			}

			@Override
			public String getUsername() {
				return "lind";
			}

			@Override
			public boolean isAccountNonExpired() {
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return false;
			}

			@Override
			public boolean isEnabled() {
				return false;
			}
		});
		log.info("token={}", token);
		log.info("verifty:{}", JwtUtils.verifierToken(token).getSignature());
	}

	@SneakyThrows
	@Test
	public void exp() {

		String day = "1631685645";
		System.out.println(stampToDate(day));
		System.out.println(dateToStamp("2021-09-25 11:00:00"));

	}

}
