package com.lind.uaa.simple.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author ruoyi
 */
@Data
public class LoginUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 用户唯一标识
	 */
	private String token;

	/**
	 * 登录时间
	 */
	private Long loginTime;

	/**
	 * 过期时间
	 */
	private Long expireTime;

	/**
	 * 登录IP地址
	 */
	private String ipaddr;

	/**
	 * 登录地点
	 */
	private String loginLocation;

	/**
	 * 浏览器类型
	 */
	private String browser;

	/**
	 * 操作系统
	 */
	private String os;

	/**
	 * 权限列表
	 */
	private Set<String> permissions;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return "";
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

}
