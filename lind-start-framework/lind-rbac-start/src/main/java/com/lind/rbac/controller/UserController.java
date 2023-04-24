package com.lind.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.dto.DateRangeDTO;
import com.lind.common.dto.PageDTO;
import com.lind.common.rest.CommonResult;
import com.lind.common.util.CopyUtils;
import com.lind.logger.anno.LogRecord;
import com.lind.rbac.dao.PermissionDao;
import com.lind.rbac.dao.RoleDao;
import com.lind.rbac.dao.UserDao;
import com.lind.rbac.dao.UserRoleDao;
import com.lind.rbac.dto.UserDTO;
import com.lind.rbac.dto.UserPasswordDTO;
import com.lind.rbac.entity.Role;
import com.lind.rbac.entity.User;
import com.lind.rbac.entity.UserRole;
import com.lind.rbac.enums.ModuleTypeCons;
import com.lind.rbac.enums.OperateTypeCons;
import com.lind.rbac.vo.UserVO;
import com.lind.redis.service.RedisService;
import com.lind.uaa.jwt.config.Constants;
import com.lind.uaa.jwt.config.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "用户接口")
@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	UserDao userDao;

	@Autowired
	PermissionDao permissionDao;

	@Autowired
	SecurityUtil securityUtil;

	@Autowired
	UserRoleDao userRoleDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	RedisService redisService;

	@Value("${defaultPassword:Abcd1234}")
	String defaultPassword;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private void fillRoleList(UserVO userVO) {
		List<String> userRoleIds = userRoleDao
				.selectList(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, userVO.getId())).stream()
				.map(o -> o.getRoleId()).collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(userRoleIds)) {
			List<Role> roles = roleDao.selectList(new QueryWrapper<Role>().lambda().in(Role::getId, userRoleIds));
			if (!CollectionUtils.isEmpty(roles))
				userVO.setRoleList(roles);
		}
	}

	@ApiOperation("列表")
	@GetMapping("list")
	public CommonResult<IPage<UserVO>> index(@ApiParam("用户名") @RequestParam(required = false) String username,
			@ApiParam("时间") DateRangeDTO rangeDTO, @ApiParam("页码") PageDTO pageParam) {

		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		Optional.ofNullable(rangeDTO.getFromDate())
				.ifPresent(o -> userQueryWrapper.lambda().ge(User::getCreateTime, o));
		Optional.ofNullable(rangeDTO.getToDate())
				.ifPresent(o -> userQueryWrapper.lambda().le(User::getCreateTime, o.plusDays(1)));
		if (StringUtils.isNotBlank(username)) {
			userQueryWrapper.lambda().like(User::getUsername, username);
		}
		userQueryWrapper.lambda().orderByDesc(User::getUpdateTime);
		Page<User> result = userDao.selectPage(new Page<>(pageParam.getPageNumber(), pageParam.getPageSize()),
				userQueryWrapper);

		List<UserVO> userVOS = CopyUtils.copyListProperties(result.getRecords(), UserVO.class);

		for (UserVO userVO : userVOS) {
			fillRoleList(userVO);
		}
		IPage<UserVO> userVOIPage = new Page();
		userVOIPage.setRecords(userVOS);
		userVOIPage.setPages(result.getPages());
		userVOIPage.setSize(result.getSize());
		userVOIPage.setTotal(result.getTotal());
		return CommonResult.ok(userVOIPage);
	}

	@SneakyThrows
	@ApiOperation("新增")
	@PostMapping("add")
	@LogRecord(dataId = "${#user.id}", dataTitle = "${#user.username}", moduleType = ModuleTypeCons.USER_MGR,
			operateType = OperateTypeCons.ADD)
	@Transactional(rollbackFor = Exception.class)
	public CommonResult add(@RequestBody @Validated UserDTO user) {
		if (userDao.selectCount(new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername()).or()
				.eq(User::getPhone, user.getPhone()).or().eq(User::getEmail, user.getEmail())) > 0) {
			return CommonResult.clientFailure(
					String.format("用户已经存在【%s %s %s】", user.getUsername(), user.getPhone(), user.getEmail()));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User userEntity = new User();
		CopyUtils.copyProperties(user, userEntity);
		userDao.insert(userEntity);
		user.setId(userEntity.getId());
		if (!CollectionUtils.isEmpty(user.getRoleIdList())) {
			for (String roleId : user.getRoleIdList()) {
				UserRole userRole = new UserRole();
				userRole.setUserId(userEntity.getId());
				userRole.setRoleId(roleId);
				userRoleDao.insert(userRole);
			}
		}
		return CommonResult.ok();
	}

	@SneakyThrows
	@ApiOperation("更新")
	@PutMapping("update/{id}")
	@LogRecord(dataId = "${#id}", dataTitle = "${#user.username}", moduleType = ModuleTypeCons.USER_MGR,
			operateType = OperateTypeCons.EDIT)
	@Transactional(rollbackFor = Exception.class)
	public CommonResult update(@ApiParam("用户ID") @PathVariable String id, @RequestBody UserDTO user) {
		User userEntity = userDao.selectById(id);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		if (userDao
				.selectCount(queryWrapper.lambda().eq(User::getUsername, user.getUsername()).ne(User::getId, id)) > 0) {
			return CommonResult.clientFailure(String.format("用户名%s已经存在", user.getUsername()));
		}
		if (userDao.selectCount(queryWrapper.lambda().eq(User::getPhone, user.getPhone()).ne(User::getId, id)) > 0) {
			return CommonResult.clientFailure(String.format("手机号%s已经存在", user.getPhone()));
		}
		if (userDao.selectCount(queryWrapper.lambda().eq(User::getEmail, user.getEmail()).ne(User::getId, id)) > 0) {
			return CommonResult.clientFailure(String.format("邮箱%s已经存在", user.getEmail()));
		}
		if (userEntity != null) {
			CopyUtils.copyProperties(user, userEntity, "password");
			Optional.ofNullable(user.getPassword()).ifPresent(o -> {
				if (StringUtils.isNoneBlank(o))
					userEntity.setPassword(passwordEncoder.encode(o));
			});
			userDao.updateById(userEntity);
			user.setUsername(userEntity.getUsername());

			if (!CollectionUtils.isEmpty(user.getRoleIdList())) {
				userRoleDao.delete(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId, id));
				for (String roleId : user.getRoleIdList()) {
					UserRole userRole = new UserRole();
					userRole.setUserId(id);
					userRole.setRoleId(roleId);
					userRoleDao.insert(userRole);
				}
			}
			redisService.del(Constants.USER_PERMISSION.concat(id));
		}
		return CommonResult.ok();
	}

	@SneakyThrows
	@ApiOperation("更新密码")
	@PutMapping("update-password")
	public CommonResult resetPassword(@ApiParam("更新密码对象") @Valid @RequestBody UserPasswordDTO user) {
		User userEntity = userDao.selectById(securityUtil.getCurrUser().getId());
		if (userEntity != null) {
			if (!passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
				return CommonResult.clientFailure("老密码不正确");
			}
			if (!user.getNewPassword().equals(user.getConfirmPassword())) {
				return CommonResult.clientFailure("新密码与确认密码不同");
			}
			userEntity.setPassword(passwordEncoder.encode(user.getNewPassword()));
			userDao.updateById(userEntity);
		}
		return CommonResult.ok();
	}

	@SneakyThrows
	@ApiOperation("重制密码")
	@PutMapping("bulk-reset-password")
	@LogRecord(dataId = "${#idList}", moduleType = ModuleTypeCons.USER_MGR, operateType = OperateTypeCons.EDIT)
	public CommonResult resetPassword(@ApiParam("用户ID列表") @RequestBody List<String> idList) {
		if (CollectionUtils.isEmpty(idList))
			return CommonResult.clientFailure("请选择要重置密码的用户");
		idList.forEach(o -> {
			User user = new User();
			user.setId(o);
			user.setPassword(passwordEncoder.encode(defaultPassword));
			userDao.updateById(user);
		});
		return CommonResult.ok();
	}

	@ApiOperation("获取用户")
	@GetMapping("{id}")
	public CommonResult<UserVO> get(@ApiParam("用户ID") @PathVariable String id) {
		User user = userDao.selectById(id);
		if (user == null) {
			return CommonResult.clientFailure("用户未找到");
		}
		UserVO userVO = CopyUtils.copyProperties(user, UserVO.class);
		fillRoleList(userVO);
		return CommonResult.ok(userVO);
	}

	@ApiOperation("删除")
	@DeleteMapping("del/{id}")
	public CommonResult del(@ApiParam("用户ID") @PathVariable String id) {
		QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
		userQueryWrapper.eq("id", id);
		userDao.delete(userQueryWrapper);
		redisService.del(Constants.USER_PERMISSION.concat(id));
		return CommonResult.ok();
	}

	@ApiOperation("批量删除")
	@PostMapping("bulk-del")
	@LogRecord(dataId = "${#ids}", moduleType = ModuleTypeCons.USER_MGR, operateType = OperateTypeCons.DEL)
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
