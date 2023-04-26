package com.lind.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.lind.common.dto.PageDTO;
import com.lind.common.rest.CommonResult;
import com.lind.common.util.CopyUtils;
import com.lind.rbac.dao.PermissionDao;
import com.lind.rbac.dao.RoleDao;
import com.lind.rbac.dao.RolePermissionDao;
import com.lind.rbac.dao.UserDao;
import com.lind.rbac.dao.UserRoleDao;
import com.lind.rbac.dto.PermissionDTO;
import com.lind.rbac.entity.Permission;
import com.lind.rbac.entity.Role;
import com.lind.rbac.entity.RolePermission;
import com.lind.rbac.entity.UserRole;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.entity.ResourcePermission;
import com.lind.uaa.jwt.service.ResourcePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "菜单管理")
@RequestMapping("permission")
public class PermissionController {

	/**
	 * 初始化数据，管理员的角色ID.
	 */
	static final String MANAGER_ROLE_ID = "1";

	@Autowired
	ResourcePermissionService resourcePermissionService;

	@Autowired
	PermissionDao permissionDao;

	@Autowired
	RedisService redisService;

	@Autowired
	RolePermissionDao rolePermissionDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	UserDao userDao;

	@Autowired
	UserRoleDao userRoleDao;

	@ApiOperation("所有树形菜单")
	@GetMapping
	public CommonResult<List<? extends ResourcePermission>> index() {
		return CommonResult.ok(resourcePermissionService.getTreeMenus());
	}

	@ApiOperation("登录用户的树形菜单")
	@GetMapping("user-tree")
	public CommonResult<List<? extends ResourcePermission>> currentUserPermissionIndex() {
		return CommonResult.ok(resourcePermissionService.getRoleTreeMenus());
	}

	/**
	 * 列表页
	 * @param pageParam json raw参数体.
	 * @return
	 */
	@ApiOperation("列表页")
	@GetMapping("query")
	public CommonResult<IPage<Permission>> query(@ApiParam("分页") PageDTO pageParam) {
		QueryWrapper<Permission> userQueryWrapper = new QueryWrapper<>();
		IPage<Permission> result = permissionDao
				.selectPage(new Page<>(pageParam.getPageNumber(), pageParam.getPageSize()), userQueryWrapper);

		return CommonResult.ok(result);
	}

	@GetMapping("list")
	public CommonResult<IPage<Permission>> list(@ApiParam("分页") PageDTO pageParam) {
		return query(pageParam);
	}

	@ApiOperation("新增")
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public CommonResult add(@RequestBody PermissionDTO permission) {
		if (permissionDao
				.selectOne(new QueryWrapper<Permission>().lambda().eq(Permission::getTitle, permission.getTitle())
						.eq(Permission::getParentId, permission.getParentId())) != null) {
			return CommonResult.clientFailure(String.format("%s在同一父级下已经存在", permission.getTitle()));
		}
		Permission permissionEntity = new Permission();
		CopyUtils.copyProperties(permission, permissionEntity);
		permissionDao.insert(permissionEntity);
		// 管理员添加新菜单权限
		RolePermission rolePermission = new RolePermission();
		rolePermission.setPermissionId(permissionEntity.getId());
		rolePermission.setRoleId(MANAGER_ROLE_ID);
		rolePermission.setSelected(false);
		rolePermissionDao.insert(rolePermission);
		delRedisPermission();
		return CommonResult.ok();
	}

	@ApiOperation("更新")
	@PutMapping("/{id}")
	public CommonResult update(@ApiParam("菜单ID") @PathVariable String id, @RequestBody PermissionDTO permission) {
		Permission permissionEntity = permissionDao.selectById(id);
		if (permissionEntity != null) {
			if (permissionDao
					.selectOne(new QueryWrapper<Permission>().lambda().eq(Permission::getTitle, permission.getTitle())
							.eq(Permission::getParentId, permission.getParentId()).ne(Permission::getId, id)) != null) {
				return CommonResult.clientFailure(String.format("%s在同一父级下已经存在", permission.getTitle()));
			}
			CopyUtils.copyProperties(permission, permissionEntity);
			permissionDao.updateById(permissionEntity);
			delRedisPermission();
		}
		return CommonResult.ok();
	}

	@ApiOperation("删除")
	@DeleteMapping("/{id}")
	public CommonResult del(@ApiParam("菜单ID") @PathVariable String id) {
		QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
		permissionDao.delete(new QueryWrapper<Permission>().lambda().eq(Permission::getId, id));
		delRedisPermission();
		return CommonResult.ok();
	}

	@ApiOperation("获取")
	@GetMapping("/{id}")
	public CommonResult<Permission> get(@ApiParam("id") @PathVariable String id) {
		return CommonResult.ok(permissionDao.selectById(id));
	}

	@ApiOperation("批量删除")
	@PostMapping("bulk-del")
	public CommonResult bulkDel(@ApiParam("ID列表") @RequestParam List<String> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return CommonResult.clientFailure("ids为空");
		}
		for (String id : ids) {
			del(id);
		}
		delRedisPermission();
		return CommonResult.ok();
	}

	@ApiOperation("菜单面包绡")
	@GetMapping("father")
	public CommonResult breadcrumb(@RequestParam(required = false) String id,
			@RequestParam(required = false) String path) {
		List<Permission> list = new ArrayList<>();
		QueryWrapper<Permission> queryWrapper = new QueryWrapper();
		if (StringUtils.isNotBlank(id)) {
			queryWrapper.lambda().eq(Permission::getId, id);
		}
		if (StringUtils.isNoneBlank(path)) {
			queryWrapper.lambda().eq(Permission::getUrl, path);
		}
		Permission permission = permissionDao.selectOne(queryWrapper);
		findFather(permission, list);
		return CommonResult.ok(Lists.reverse(list));
	}

	/**
	 * 递归找爸爸.
	 * @param current
	 * @param list
	 */
	void findFather(Permission current, List<Permission> list) {
		if (current != null) {
			list.add(current);
			if (StringUtils.isNotBlank(current.getParentId())) {
				Permission father = permissionDao.selectById(current.getParentId());
				if (father != null) {
					findFather(father, list);
				}
			}
		}
	}

	/**
	 * 清除缓存.
	 */
	void delRedisPermission() {
		// 所有菜单缓存
		redisService.del(Constants.PERMISSION_ALL);
		// 管理员角色菜单缓存
		redisService.del(Constants.ROLE_PERMISSION.concat(MANAGER_ROLE_ID));
		// 管理员角色用户的菜单缓存
		userRoleDao.selectList(new QueryWrapper<UserRole>().lambda().eq(UserRole::getRoleId, MANAGER_ROLE_ID))
				.forEach(o -> {
					redisService.del(Constants.USER_PERMISSION.concat(o.getUserId()));
				});

	}

}
