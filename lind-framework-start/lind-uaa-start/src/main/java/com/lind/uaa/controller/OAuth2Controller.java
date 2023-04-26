package com.lind.uaa.controller;

import com.lind.uaa.entity.ResourceUser;
import com.lind.uaa.redis.RedisUtil;
import com.lind.uaa.util.UAAConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.lind.uaa.util.UAAConstant.LOGIN_ACCOUNT;

@RestController
@Slf4j
@SuppressWarnings("unchecked")
public class OAuth2Controller {

	@Autowired
	private TokenEndpoint tokenEndpoint;

	@Autowired
	private RedisUtil redisService;

	@PostMapping("/oauth/login")
	public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal,
			@RequestParam Map<String, String> parameters) {
		try {
			ResponseEntity<OAuth2AccessToken> responseEntity = tokenEndpoint.postAccessToken(principal, parameters);
			if (!ObjectUtils.isEmpty(responseEntity)) {
				if (responseEntity.getStatusCodeValue() == 200) {
					ResourceUser user = (ResourceUser) redisService
							.get(UAAConstant.USER + responseEntity.getBody().getValue());
					if (ObjectUtils.isEmpty(user)) {
						ResourceUser user1 = (ResourceUser) redisService
								.get(UAAConstant.USER + parameters.get(LOGIN_ACCOUNT));
						if (!ObjectUtils.isEmpty(user1))
							redisService.set(UAAConstant.USER + responseEntity.getBody().getValue(), user1, 86400L);
					}

				}
			}
			return responseEntity;

		}
		catch (InvalidGrantException e) {
			Map map = new HashMap();
			map.put("code", 500);
			map.put("message", "账户名或密码错误!");
			e.printStackTrace();
			return new ResponseEntity(map, HttpStatus.OK);
		}
		catch (UsernameNotFoundException e) {
			Map map = new HashMap();
			map.put("code", 500);
			map.put("message", "账户名不存在!");
			e.printStackTrace();
			return new ResponseEntity(map, HttpStatus.OK);
		}
		catch (InternalAuthenticationServiceException e) {
			Map map = new HashMap();
			map.put("code", 500);
			map.put("message", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity(map, HttpStatus.OK);
		}
		catch (Exception e) {
			Map map = new HashMap();
			map.put("code", 500);
			map.put("message", "服务端异常，请联系管理员");
			e.printStackTrace();
			return new ResponseEntity(map, HttpStatus.OK);
		}
	}

	/**
	 * 当前登陆用户信息<br>
	 * <p>
	 * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()<br>
	 * 返回值是接口org.springframework.security.core.Authentication，又继承了Principal<br>
	 * 这里的实现类是org.springframework.security.oauth2.provider.OAuth2Authentication<br>
	 * <p>
	 * 因此这只是一种写法，下面注释掉的三个方法也都一样，这四个方法任选其一即可，也只能选一个，毕竟uri相同，否则启动报错<br>
	 * 2018.05.23改为默认用这个方法，好理解一点
	 * @return
	 */
	@GetMapping("/oauth/user-me")
	public Authentication principal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}

}
