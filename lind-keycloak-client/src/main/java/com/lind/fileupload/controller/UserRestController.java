package com.lind.fileupload.controller;

import com.lind.fileupload.dto.UserModifyPasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@PutMapping("/update-password")
	public ResponseEntity updatePassword(@Valid @RequestBody UserModifyPasswordDTO userModifyPasswordDTO) {
		return ResponseEntity.ok("hello");
	}

}
