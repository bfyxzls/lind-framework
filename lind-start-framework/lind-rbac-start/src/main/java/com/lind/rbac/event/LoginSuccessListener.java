package com.lind.rbac.event;

import com.lind.uaa.jwt.entity.ResourceUser;
import com.lind.uaa.jwt.entity.TokenResult;
import com.lind.uaa.jwt.event.LoginSuccessEvent;
import com.lind.uaa.jwt.service.JwtUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoginSuccessListener {

	@Autowired
	JwtUserService jwtUserService;

	@EventListener
	public void onApplicationEvent(LoginSuccessEvent event) {
		TokenResult tokenResult = event.getTokenResult();
		System.out.println("login success:" + tokenResult.getSubject() + ",token:" + tokenResult.getToken());
		log.info("login success\nname:{}\ntoken:{}\n", tokenResult.getSubject(), tokenResult.getToken());
		ResourceUser userDetails = jwtUserService.getUserDetailsByToken(tokenResult.getToken());
		log.info("user:{}", userDetails);
	}

}
