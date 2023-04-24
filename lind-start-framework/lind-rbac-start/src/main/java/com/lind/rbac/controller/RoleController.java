package com.lind.rbac.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.dto.PageDTO;
import com.lind.common.rest.CommonResult;
import com.lind.common.util.CopyUtils;
import com.lind.rbac.dao.RoleDao;
import com.lind.rbac.dao.RolePermissionDao;
import com.lind.rbac.dto.RoleDTO;
import com.lind.rbac.entity.Role;
import com.lind.rbac.entity.RolePermission;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.service.ResourcePermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
public class RoleController {

	@Autowired
	RoleDao roleDao;

	@Autowired
	RolePermissionDao rolePermissionDao;

	@Autowired
	ResourcePermissionService resourcePermissionService;

	@Autowired
	RedisService redisService;

	/**
	 * 更新角色的菜单列表.
	 * @param permissionList
	 * @param role
	 */
	private void updateRolePermissions(List<String> permissionList, Role role) {
		if (!CollectionUtil.isNotEmpty(permissionList)) {
			rolePermissionDao
					.delete(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId, role.getId()));
			permissionList.forEach(o -> {
				RolePermission rolePermission = new RolePermission();
				rolePermission.setPermissionId(o);
				rolePermission.setRoleId(role.getId());
				rolePermission.setSelected(false);
				rolePermissionDao.insert(rolePermission);
			});
			redisService.del(Constants.ROLE_PERMISSION.concat(role.getId()));
		}
	}

	@ApiOperation("列表")
	@GetMapping("list")
	public CommonResult<IPage<RoleDTO>> index(@ApiParam("名称") @RequestParam(required = false) String name,
			@ApiParam("页码") PageDTO pageParam) {
		QueryWrapper<Role> userQueryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(name)) {
			userQueryWrapper.lambda().like(Role::getName, name);
		}
		IPage<Role> roleList = roleDao.selectPage(new Page<>(pageParam.getPageNumber(), pageParam.getPageSize()),
				userQueryWrapper);

		List<RoleDTO> roles = roleList.getRecords().stream().map(o -> {
			RoleDTO role = new RoleDTO();
			role.setId(o.getId());
			role.setName(o.getName());
			role.setPermissionList(resourcePermissionService.getAllByRoleId(o.getId()));
			return role;
		}).collect(Collectors.toList());

		IPage<RoleDTO> roleDTOIPage = new Page();
		roleDTOIPage.setRecords(roles);
		roleDTOIPage.setPages(roleList.getPages());
		roleDTOIPage.setSize(roleList.getSize());
		roleDTOIPage.setTotal(roleList.getTotal());
		return CommonResult.ok(roleDTOIPage);
	}

	@ApiOperation("新增")
	@PostMapping("add")
	@Transactional(rollbackFor = Exception.class)
	public CommonResult add(@Valid @RequestBody RoleDTO roleDTO) {
		if (roleDao.selectOne(new QueryWrapper<Role>().lambda().eq(Role::getName, roleDTO.getName())) != null) {
			return CommonResult.clientFailure(String.format("%s已经存在", roleDTO.getName()));
		}
		Role role = new Role();
		CopyUtils.copyProperties(roleDTO, role);
		roleDao.insert(role);
		updateRolePermissions(roleDTO.getPermissionIdList(), role);
		return CommonResult.ok();
	}

	@ApiOperation("更新")
	@PutMapping("update/{id}")
	@Transactional(rollbackFor = Exception.class)
	public CommonResult update(@ApiParam("角色ID") @PathVariable String id, @RequestBody RoleDTO roleDTO) {
		Role role = roleDao.selectById(id);
		if (role != null) {
			if (roleDao.selectOne(new QueryWrapper<Role>().lambda().ne(Role::getId, id).eq(Role::getName,
					roleDTO.getName())) != null) {
				return CommonResult.clientFailure(String.format("%s已经存在", roleDTO.getName()));
			}
			CopyUtils.copyProperties(roleDTO, role);
			roleDao.updateById(role);
			updateRolePermissions(roleDTO.getPermissionIdList(), role);
			redisService.del(Constants.ROLE_PERMISSION.concat(id));
		}
		return CommonResult.ok();
	}

	@ApiOperation("更新角色的权限")
	@PutMapping("update/{id}/permission")
	@Transactional
	public CommonResult updatePermission(@ApiParam("角色ID") @PathVariable String id,
			@RequestBody List<String> permissions) {
		Role role = roleDao.selectById(id);
		if (role != null) {
			updateRolePermissions(permissions, role);
		}
		return CommonResult.ok();
	}

	@ApiOperation("删除")
	@DeleteMapping("del/{id}")
	public CommonResult del(@ApiParam("角色ID") @PathVariable String id) {
		QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
		roleQueryWrapper.eq("id", id);
		roleDao.delete(roleQueryWrapper);
		redisService.del(Constants.ROLE_PERMISSION.concat(id));
		return CommonResult.ok();
	}

	@ApiOperation("获取")
	@GetMapping("/{id}")
	public CommonResult<Role> get(@ApiParam("角色ID") @PathVariable String id) {
		return CommonResult.ok(roleDao.selectById(id));
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
		return CommonResult.ok();
	}

}
