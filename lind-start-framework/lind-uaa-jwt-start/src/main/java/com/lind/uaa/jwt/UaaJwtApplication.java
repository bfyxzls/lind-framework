package com.lind.uaa.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lind.uaa.jwt.utils.JwtUtils;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@SpringBootApplication
@EnableCaching
@RestController
public class UaaJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaaJwtApplication.class, args);
	}

	@SneakyThrows
	@GetMapping("/test-verify")
	public DecodedJWT verify(String token) {
		return JwtUtils.verifierToken(token);
	}

	@GetMapping("/test")
	@SneakyThrows
	public String test() {
		return JwtUtils.generTokenByRS256(new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
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
	}

}
