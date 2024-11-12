package com.lind.hibernate.service;

import com.lind.hibernate.dto.UpdateUser;

public interface UserService {

	<T extends UpdateUser> void updateUserInfo(T userInfo);

}
