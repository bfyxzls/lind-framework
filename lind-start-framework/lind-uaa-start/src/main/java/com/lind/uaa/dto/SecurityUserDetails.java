package com.lind.uaa.dto;

import com.lind.uaa.entity.ResourcePermission;
import com.lind.uaa.entity.ResourceRole;
import com.lind.uaa.entity.ResourceUser;
import com.lind.uaa.util.UAAException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户认证.
 */
@Slf4j
public class SecurityUserDetails implements UserDetails {

	private ResourceUser user;

	public SecurityUserDetails(ResourceUser user) {
		if (user == null) {
			throw new UAAException("请实现com.lind.uaa.entity.User接口");
		}
		this.user = user;
	}

	/**
	 * 添加用户拥有的权限和角色
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authorityList = new ArrayList<>();
		List<ResourcePermission> resourcePermissions = user.getResourcePermissions();
		// 添加请求权限
		if (resourcePermissions != null && resourcePermissions.size() > 0) {
			for (ResourcePermission resourcePermission : resourcePermissions) {
				if (StringUtils.isNotBlank(resourcePermission.getTitle())
						&& StringUtils.isNotBlank(resourcePermission.getPath())) {
					authorityList.add(new SimpleGrantedAuthority(resourcePermission.getTitle()));
				}
			}
		}
		// 添加角色
		List<ResourceRole> resourceRoles = user.getResourceRoles();
		if (resourceRoles != null && resourceRoles.size() > 0) {
			// lambda表达式
			resourceRoles.forEach(item -> {
				if (StringUtils.isNotBlank(item.getName())) {
					authorityList.add(new SimpleGrantedAuthority(item.getName()));
				}
			});
		}
		return authorityList;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	/**
	 * 账户是否过期
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	/**
	 * 是否禁用
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	/**
	 * 密码是否过期
	 * @return
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否启用
	 * @return
	 */
	@Override
	public boolean isEnabled() {

		return true;
	}

}