package com.lind.uaa.simple.service;

import cn.hutool.core.util.StrUtil;
import com.lind.uaa.simple.execption.ServiceException;
import com.lind.uaa.simple.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 安全服务工具类
 *
 * @author ruoyi
 */
public class SecurityUtils {

	/**
	 * 所有权限标识
	 */
	public static final String ALL_PERMISSION = "*:*:*";
	/**
	 * 管理员角色权限标识
	 */
	public static final String SUPER_ADMIN = "admin";

	/**
	 * 用户ID
	 **/
	public static Long getUserId() {
		try {
			return getLoginUser().getUserId();
		}
		catch (Exception e) {
			throw new ServiceException("获取用户ID异常", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * 获取部门ID
	 **/
	public static Long getDeptId() {
		try {
			return getLoginUser().getDeptId();
		}
		catch (Exception e) {
			throw new ServiceException("获取部门ID异常", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * 获取用户账户
	 **/
	public static String getUsername() {
		try {
			return getLoginUser().getUsername();
		}
		catch (Exception e) {
			throw new ServiceException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * 获取用户
	 **/
	public static LoginUser getLoginUser() {
		try {
			return (LoginUser) getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * 获取Authentication
	 */
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 生成BCryptPasswordEncoder密码
	 * @param password 密码
	 * @return 加密字符串
	 */
	public static String encryptPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	/**
	 * 判断密码是否相同
	 * @param rawPassword 真实密码
	 * @param encodedPassword 加密后字符
	 * @return 结果
	 */
	public static boolean matchesPassword(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	/**
	 * 是否为管理员
	 * @param userId 用户ID
	 * @return 结果
	 */
	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}

	/**
	 * 验证用户是否具备某权限
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public static boolean hasPermi(String permission) {
		return hasPermi(getLoginUser().getPermissions(), permission);
	}

	/**
	 * 判断是否包含权限
	 * @param authorities 权限列表
	 * @param permission 权限字符串
	 * @return 用户是否具备某权限
	 */
	public static boolean hasPermi(Collection<String> authorities, String permission) {
		return authorities.stream().filter(StrUtil::isNotEmpty)
				.anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
	}

	/**
	 * 验证用户是否拥有某个角色
	 * @param role 角色标识
	 * @return 用户是否具备某角色
	 */
	public static boolean hasRole(String role) {
		Collection<? extends GrantedAuthority> roleList = getLoginUser().getAuthorities();
		Collection<String> roles = roleList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return hasRole(roles, role);
	}

	/**
	 * 判断是否包含角色
	 * @param roles 角色列表
	 * @param role 角色
	 * @return 用户是否具备某角色权限
	 */
	public static boolean hasRole(Collection<String> roles, String role) {
		return roles.stream().filter(StrUtil::isNotEmpty)
				.anyMatch(x -> SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
	}

}
