package com.lind.upms.web.service;

import com.lind.upms.core.domain.entity.SysUser;
import com.lind.upms.core.domain.model.LoginUser;
import com.lind.upms.enums.UserStatus;
import com.lind.upms.exception.ServiceException;
import com.lind.upms.service.ISysUserService;
import com.lind.upms.utils.MessageUtils;
import com.lind.upms.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private ISysUserService userService;

	@Autowired
	private SysPasswordService passwordService;

	@Autowired
	private SysPermissionService permissionService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = userService.selectUserByUserName(username);
		if (StringUtils.isNull(user)) {
			log.info("登录用户：{} 不存在.", username);
			throw new ServiceException(MessageUtils.message("user.not.exists"));
		}
		else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
			log.info("登录用户：{} 已被删除.", username);
			throw new ServiceException(MessageUtils.message("user.password.delete"));
		}
		else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
			log.info("登录用户：{} 已被停用.", username);
			throw new ServiceException(MessageUtils.message("user.blocked"));
		}

		passwordService.validate(user);

		return createLoginUser(user);
	}

	public UserDetails createLoginUser(SysUser user) {
		return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
	}

}
