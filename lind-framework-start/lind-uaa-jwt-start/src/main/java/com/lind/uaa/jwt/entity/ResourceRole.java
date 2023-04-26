package com.lind.uaa.jwt.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lind.uaa.jwt.entity.serialize.ResourceRoleDeserializer;
import com.lind.uaa.jwt.entity.serialize.ResourceRoleSerializer;

import java.io.Serializable;

/**
 * 角色.
 */
@JsonDeserialize(using = ResourceRoleDeserializer.class)
@JsonSerialize(using = ResourceRoleSerializer.class)
public interface ResourceRole extends Serializable {

	/**
	 * 角色ID.
	 * @return
	 */
	String getId();

	/**
	 * 角色名称.
	 * @return
	 */
	String getName();

}
