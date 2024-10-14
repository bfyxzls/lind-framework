package com.lind.fileupload.service;

import com.lind.fileupload.dto.UpdateUser;
import com.lind.fileupload.dto.UpdateUserInfoDTO;
import com.lind.fileupload.dto.UpdateUserModifyPasswordDTO;
import org.springframework.stereotype.Service;

/**
 * @author lind
 * @date 2024/10/14 15:09
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

	public <T extends UpdateUser> void updateUserInfo(T userInfo) {
		if (userInfo instanceof UpdateUserInfoDTO) {
			System.out.println("update user info");
		}
		if (userInfo instanceof UpdateUserModifyPasswordDTO) {
			System.out.println("update user password");
		}
	}

}
