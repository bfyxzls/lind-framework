package com.lind.uaa.service;

import com.lind.uaa.entity.ResourcePermission;

import java.util.List;

public interface OauthPermissionService {

	List<ResourcePermission> getByType(int type);

}
