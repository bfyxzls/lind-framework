package com.lind.hibernate.service;

import com.lind.hibernate.dto.UpdateUser;
import com.lind.hibernate.dto.UpdateUserInfoDTO;
import com.lind.hibernate.dto.UpdateUserModifyPasswordDTO;
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
