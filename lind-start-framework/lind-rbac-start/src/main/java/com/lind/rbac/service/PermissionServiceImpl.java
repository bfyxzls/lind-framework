package com.lind.rbac.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lind.rbac.dao.PermissionDao;
import com.lind.rbac.dao.RoleDao;
import com.lind.rbac.dao.RolePermissionDao;
import com.lind.rbac.dao.UserRoleDao;
import com.lind.rbac.entity.Permission;
import com.lind.rbac.entity.Role;
import com.lind.rbac.entity.RolePermission;
import com.lind.rbac.entity.UserRole;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.config.SecurityUtil;
import com.lind.uaa.jwt.entity.ResourcePermission;
import com.lind.uaa.jwt.service.ResourcePermissionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements ResourcePermissionService {

	@Autowired
	PermissionDao permissionDao;

	@Autowired
	RolePermissionDao rolePermissionDao;

	@Autowired
	UserRoleDao userRoleDao;

	@Autowired
	SecurityUtil securityUtil;

	@Autowired
	RoleDao roleDao;

	@Autowired
	RedisService redisService;

	@SneakyThrows
	@Override
	public Set<? extends ResourcePermission> getUserAll() {
		// TODO:需要实现树逻辑，用户角色过滤
		String key = Constants.USER_PERMISSION + securityUtil.getCurrUser().getId();
		if (redisService.hasKey(key)) {
			Set<? extends ResourcePermission> resourcePermissions = (Set<ResourcePermission>) redisService.get(key);
			return resourcePermissions;
		}
		List<String> roleIdList = new ArrayList<>();
		List<UserRole> userRoles = userRoleDao.selectList(
				new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, securityUtil.getCurrUser().getId()));
		if (!org.springframework.util.CollectionUtils.isEmpty(userRoles)) {
			List<Role> roles = roleDao.selectList(new QueryWrapper<Role>().lambda().in(Role::getId,
					userRoles.stream().map(o -> o.getRoleId()).collect(Collectors.toList())));

			if (!org.springframework.util.CollectionUtils.isEmpty(roles)) {

				for (Role o : roles) {
					roleIdList.add(o.getId());
				}
			}
		}
		List<RolePermission> rolePermissions = rolePermissionDao
				.selectList(new QueryWrapper<RolePermission>().lambda().in(RolePermission::getRoleId, roleIdList));
		Set<String> permissionIds = new HashSet<>();
		rolePermissions.forEach(o -> {
			permissionIds.add(o.getPermissionId());
		});
		List<Permission> list = permissionDao
				.selectList(new QueryWrapper<Permission>().lambda().in(Permission::getId, permissionIds));
		Set<Permission> result = new HashSet<>();
		for (Permission permission : list) {
			Permission permission1 = Permission.builder().title(permission.getTitle())
					.parentId(permission.getParentId()).type(permission.getType()).icon(permission.getIcon())
					.sortNumber(permission.getSortNumber()).url(permission.getUrl())
					.permissions(permission.getPermissions()).build();
			permission1.setId(permission.getId());
			result.add(permission1);
		}
		redisService.set(key, result);
		return result;
	}

	@Override
	public List<Permission> getAll() {
		return permissionDao.selectList(new QueryWrapper<>());
	}

	@Override
	public List<? extends ResourcePermission> getAllByRoleId(String roleId) {
		QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(RolePermission::getRoleId, roleId);
		List<RolePermission> list = rolePermissionDao.selectList(queryWrapper);
		if (CollectionUtils.isNotEmpty(list)) {
			List<String> permissionIdList = list.stream().map(o -> o.getPermissionId()).collect(Collectors.toList());
			QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
			permissionQueryWrapper.lambda().in(Permission::getId, permissionIdList);
			return permissionDao.selectList(permissionQueryWrapper);
		}
		return null;
	}

}
