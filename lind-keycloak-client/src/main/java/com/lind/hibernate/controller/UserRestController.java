package com.lind.hibernate.controller;

import com.lind.hibernate.dto.UpdateUserInfoDTO;
import com.lind.hibernate.dto.UpdateUserModifyPasswordDTO;
import com.lind.hibernate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户控制器.
 *
 * @author lind
 * @date 2024/10/14 10:51
 * @since 1.0.0
 */
@RestController
@RequestMapping("user")
public class UserRestController {

	@Autowired
	UserService userService;

	/**
	 * 更新密码
	 * @param userModifyPasswordDTO
	 * @return
	 */
	@PutMapping("/update-password")
	public ResponseEntity updatePassword(@Valid @RequestBody UpdateUserModifyPasswordDTO userModifyPasswordDTO) {
		userService.updateUserInfo(userModifyPasswordDTO);
		return ResponseEntity.ok("hello");
	}

	/**
	 * 更新基本信息
	 * @param updateUserInfoDTO
	 * @return
	 */
	@PutMapping
	public ResponseEntity updateInfo(@Valid @RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
		userService.updateUserInfo(updateUserInfoDTO);
		return ResponseEntity.ok("hello");
	}

}
