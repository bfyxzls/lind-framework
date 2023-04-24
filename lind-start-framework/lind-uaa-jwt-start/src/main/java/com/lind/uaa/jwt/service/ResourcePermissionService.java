package com.lind.uaa.jwt.service;

import com.lind.uaa.jwt.entity.ResourcePermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单与按钮权限接口.
 */
public interface ResourcePermissionService {

	/**
	 * 登录用户的菜单和按钮权限.
	 * @return
	 */
	Set<? extends ResourcePermission> getUserAll();

	/**
	 * 获取所有资源权限.
	 * @return
	 */
	List<? extends ResourcePermission> getAll();

	/**
	 * 得到树型菜单结构.
	 * @return
	 */
	default List<? extends ResourcePermission> getTreeMenus() {
		List<ResourcePermission> ones = getAll().stream()
				.filter(o -> o.getParentId() == null || StringUtils.isEmpty(o.getParentId()))
				.collect(Collectors.toList());
		getTreeMenuRzSons(ones);
		return ones;
	}

	/**
	 * 得到具有角色控制的树型菜单结构.
	 * @return
	 */
	default List<? extends ResourcePermission> getRoleTreeMenus() {
		List<ResourcePermission> ones = getUserAll().stream()
				.filter(o -> o.getParentId() == null || StringUtils.isEmpty(o.getParentId()))
				.collect(Collectors.toList());
		getRoleTreeMenuRzSons(ones);
		return ones;
	}

	/**
	 * 根据角色取权限.
	 * @param roleId
	 * @return
	 */
	List<? extends ResourcePermission> getAllByRoleId(String roleId);

	/**
	 * 递归找子孙.
	 * @param ones
	 */
	default void getTreeMenuRzSons(List<? extends ResourcePermission> ones) {
		for (ResourcePermission resourcePermission : ones) {
			if (resourcePermission != null) {
				List<ResourcePermission> sons = getAll()
						.stream().filter(o -> o.getParentId() != null
								&& o.getParentId().equals(resourcePermission.getId()) && o.getType() == 0)
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sons)) {
					resourcePermission.setSons(sons);
					getTreeMenuRzSons(sons);
				}
			}
		}
	}

	/**
	 * 得到用户角色下的菜单.
	 * @param ones
	 */
	default void getRoleTreeMenuRzSons(List<? extends ResourcePermission> ones) {
		for (ResourcePermission resourcePermission : ones) {
			if (resourcePermission != null) {
				List<ResourcePermission> sons = getUserAll()
						.stream().filter(o -> o.getParentId() != null
								&& o.getParentId().equals(resourcePermission.getId()) && o.getType() == 0)
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sons)) {
					resourcePermission.setSons(sons);
					getRoleTreeMenuRzSons(sons);
				}
			}
		}
	}

	/**
	 * 递归找祖宗，并添加到列表中.
	 * @param current
	 * @param list
	 */
	default void findFather(ResourcePermission current, List<ResourcePermission> list) {
		if (current != null) {
			list.add(current);
			if (current.getParentId() != null && StringUtils.isNoneBlank(current.getParentId())) {
				ResourcePermission father = getAll().stream().filter(o -> o.getId().equals(current.getParentId()))
						.findFirst().orElse(null);
				if (father != null) {
					findFather(father, list);
				}
			}
		}
	}

}
