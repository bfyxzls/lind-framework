package com.lind.uaa.simple.handle;

import com.alibaba.fastjson2.JSON;
import com.lind.uaa.simple.model.LoginUser;
import com.lind.uaa.simple.service.TokenService;
import com.lind.uaa.simple.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	@Autowired
	private TokenService tokenService;

	/**
	 * 退出处理
	 * @return
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		LoginUser loginUser = tokenService.getLoginUser(request);
		if (loginUser != null) {
			String userName = loginUser.getUsername();
			// 删除用户缓存记录
			tokenService.delLoginUser(loginUser.getToken());
		}
		ServletUtils.renderString(response, JSON.toJSONString(new HashMap<String, Object>() {
			{	put("code", 200);
				put("msg", "user.logout.success");
			}
		}));
	}

}
