package com.lind.uaa.jwt.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lind.uaa.jwt.entity.serialize.ResourceUserDeserializer;
import com.lind.uaa.jwt.entity.serialize.ResourceUserSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户.
 */
@JsonDeserialize(using = ResourceUserDeserializer.class)
@JsonSerialize(using = ResourceUserSerializer.class)
public interface ResourceUser extends UserDetails {

	String getEmail();

	String getId();

	/**
	 * 返回用户角色.
	 * @return
	 */
	List<? extends ResourceRole> getResourceRoles();

	/**
	 * 设置用户角色.
	 * @param resourceRoles
	 */
	void setResourceRoles(List<? extends ResourceRole> resourceRoles);

	/**
	 * 将用户角色进行返回
	 * @return
	 */
	default Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		List<? extends ResourceRole> role = getResourceRoles();
		// 添加权限（角色）
		if (role != null && role.size() > 0) {
			for (ResourceRole resourceRole : role) {
				if (StringUtils.isNotBlank(resourceRole.getName())) {
					authorityList.add(new SimpleGrantedAuthority(resourceRole.getName()));
				}
			}
		}
		return authorityList;
	}

	default boolean isAccountNonExpired() {
		return true;
	}

	default boolean isAccountNonLocked() {
		return true;
	}

	default boolean isCredentialsNonExpired() {
		return true;
	}

	default boolean isEnabled() {
		return true;
	}

}
