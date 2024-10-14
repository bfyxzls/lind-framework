package com.lind.fileupload.service;

import com.lind.fileupload.dto.UpdateUser;

public interface UserService {

	<T extends UpdateUser> void updateUserInfo(T userInfo);

}
