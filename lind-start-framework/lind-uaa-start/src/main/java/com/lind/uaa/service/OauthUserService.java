package com.lind.uaa.service;

import com.lind.uaa.entity.ResourceUser;

public interface OauthUserService {

	ResourceUser getByUserName(String username);

}
