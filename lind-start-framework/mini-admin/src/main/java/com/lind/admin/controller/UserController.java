package com.lind.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lind.admin.annotation.PermissionLimit;
import com.lind.admin.dao.PermissionDao;
import com.lind.admin.entity.Permission;
import com.lind.admin.entity.User;
import com.lind.admin.service.LoginService;
import com.lind.admin.service.UserService;
import com.lind.admin.util.I18nUtil;
import com.lind.admin.util.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2019-05-04 16:39:50
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private PermissionDao permissionDao;

	@RequestMapping
	@PermissionLimit()
	public String index(Model model) {
		List<Permission> permissions = permissionDao.selectList(new QueryWrapper<>());
		model.addAttribute("permissions", permissions);
		return "user/user.index";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	@PermissionLimit()
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length, String username, int role) {

		// page list
		List<User> list = userService.pageList(start, length, username, role);
		long list_count = userService.pageListCount(start, length, username, role);

		// filter
		if (list != null && list.size() > 0) {
			for (User item : list) {
				item.setPassword(null);
			}
		}

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count); // 总记录数
		maps.put("recordsFiltered", list_count); // 过滤后的总记录数
		maps.put("data", list); // 分页列表
		return maps;
	}

	@RequestMapping("/add")
	@ResponseBody
	@PermissionLimit(adminuser = true)
	public ReturnT<String> add(User xxlJobUser) {

		// valid username
		if (!StringUtils.hasText(xxlJobUser.getUsername())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE,
					I18nUtil.getString("system_please_input") + I18nUtil.getString("user_username"));
		}
		xxlJobUser.setUsername(xxlJobUser.getUsername().trim());
		if (!(xxlJobUser.getUsername().length() >= 4 && xxlJobUser.getUsername().length() <= 20)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
		}
		// valid password
		if (!StringUtils.hasText(xxlJobUser.getPassword())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE,
					I18nUtil.getString("system_please_input") + I18nUtil.getString("user_password"));
		}
		xxlJobUser.setPassword(xxlJobUser.getPassword().trim());
		if (!(xxlJobUser.getPassword().length() >= 4 && xxlJobUser.getPassword().length() <= 20)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
		}
		// md5 password
		xxlJobUser.setPassword(DigestUtils.md5DigestAsHex(xxlJobUser.getPassword().getBytes()));

		// check repeat
		User existUser = userService.loadByUserName(xxlJobUser.getUsername());
		if (existUser != null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("user_username_repeat"));
		}

		String[] roleStrList = xxlJobUser.getPermission().split(",");
		Collection<Integer> roleIdList = new ArrayList<>();
		for (String s : roleStrList) {
			roleIdList.add(Integer.parseInt(s));
		}
		QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().in(Permission::getId, roleIdList);
		List<Permission> permissions = permissionDao.selectList(queryWrapper);
		List<String> permissionCodeList = new ArrayList<>();
		if (permissions != null) {
			permissions.forEach(o -> permissionCodeList.add(o.getPermissionCode()));
		}
		String permissionCodes = String.join(",", permissionCodeList);
		xxlJobUser.setPermission(permissionCodes);

		// write
		userService.save(xxlJobUser);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/update")
	@ResponseBody
	@PermissionLimit(adminuser = true)
	public ReturnT<String> update(HttpServletRequest request, User xxlJobUser) {

		// avoid opt login seft
		User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		if (loginUser.getUsername().equals(xxlJobUser.getUsername())) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
		}

		// valid password
		if (StringUtils.hasText(xxlJobUser.getPassword())) {
			xxlJobUser.setPassword(xxlJobUser.getPassword().trim());
			if (!(xxlJobUser.getPassword().length() >= 4 && xxlJobUser.getPassword().length() <= 20)) {
				return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
			}
			// md5 password
			xxlJobUser.setPassword(DigestUtils.md5DigestAsHex(xxlJobUser.getPassword().getBytes()));
		}
		else {
			xxlJobUser.setPassword(null);
		}

		// write
		userService.update(xxlJobUser);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/remove")
	@ResponseBody
	@PermissionLimit(adminuser = true)
	public ReturnT<String> remove(HttpServletRequest request, int id) {

		// avoid opt login seft
		User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);
		if (loginUser.getId() == id) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), I18nUtil.getString("user_update_loginuser_limit"));
		}

		userService.delete(id);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/updatePwd")
	@ResponseBody
	public ReturnT<String> updatePwd(HttpServletRequest request, String password) {

		// valid password
		if (password == null || password.trim().length() == 0) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
		}
		password = password.trim();
		if (!(password.length() >= 4 && password.length() <= 20)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, I18nUtil.getString("system_lengh_limit") + "[4-20]");
		}

		// md5 password
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

		// update pwd
		User loginUser = (User) request.getAttribute(LoginService.LOGIN_IDENTITY_KEY);

		// do write
		User existUser = userService.loadByUserName(loginUser.getUsername());
		existUser.setPassword(md5Password);
		userService.update(existUser);

		return ReturnT.SUCCESS;
	}

}
