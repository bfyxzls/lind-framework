package com.lind.admin.service;

import com.lind.admin.entity.User;
import com.lind.admin.util.CookieUtil;
import com.lind.admin.util.I18nUtil;
import com.lind.admin.util.JacksonUtil;
import com.lind.admin.util.ReturnT;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2019-05-04 22:13:264
 */
@Configuration
public class LoginService {

	public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";

	@Resource
	private UserService userService;

	private String makeToken(User xxlJobUser) {
		String tokenJson = JacksonUtil.writeValueAsString(xxlJobUser);
		String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
		return tokenHex;
	}

	private User parseToken(String tokenHex) {
		User xxlJobUser = null;
		if (tokenHex != null) {
			String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray()); // username_password(md5)
			xxlJobUser = JacksonUtil.readValue(tokenJson, User.class);
		}
		return xxlJobUser;
	}

	public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, String username,
			String password, boolean ifRemember) {

		// param
		if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) {
			return new ReturnT<String>(500, I18nUtil.getString("login_param_empty"));
		}

		// valid passowrd
		User xxlJobUser = userService.loadByUserName(username);
		if (xxlJobUser == null) {
			return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
		}
		String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!passwordMd5.equals(xxlJobUser.getPassword())) {
			return new ReturnT<String>(500, I18nUtil.getString("login_param_unvalid"));
		}

		String loginToken = makeToken(xxlJobUser);

		// do login
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
		return ReturnT.SUCCESS;
	}

	/**
	 * logout
	 * @param request
	 * @param response
	 */
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
		return ReturnT.SUCCESS;
	}

	/**
	 * logout
	 * @param request
	 * @return
	 */
	public User ifLogin(HttpServletRequest request, HttpServletResponse response) {
		String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		if (cookieToken != null) {
			User cookieUser = null;
			try {
				cookieUser = parseToken(cookieToken);
			}
			catch (Exception e) {
				logout(request, response);
			}
			if (cookieUser != null) {
				User dbUser = userService.loadByUserName(cookieUser.getUsername());
				if (dbUser != null) {
					if (cookieUser.getPassword().equals(dbUser.getPassword())) {
						return dbUser;
					}
				}
			}
		}
		return null;
	}

	public Map<String, Object> dashboardInfo() {

		Map<String, Object> dashboardMap = new HashMap<String, Object>();
		dashboardMap.put("jobInfoCount", 1);
		dashboardMap.put("jobLogCount", 10);
		dashboardMap.put("jobLogSuccessCount", 15);
		dashboardMap.put("executorCount", 8);
		return dashboardMap;
	}

	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {

		// process
		List<String> triggerDayList = Arrays.asList("2022-11-12", "2022-11-13", "2022-11-14", "2022-11-15",
				"2022-11-16", "2022-11-17");
		List<Integer> triggerDayCountRunningList = Arrays.asList(1, 3, 7, 2, 9, 1);
		List<Integer> triggerDayCountSucList = Arrays.asList(1, 13, 27, 12, 9, 1);
		List<Integer> triggerDayCountFailList = Arrays.asList(1, 3, 71, 22, 19, 1);
		int triggerCountRunningTotal = 0;
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountRunningList", triggerDayCountRunningList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);

		result.put("triggerCountRunningTotal", triggerCountRunningTotal);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);

		return new ReturnT<Map<String, Object>>(result);
	}

}
