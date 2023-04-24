package com.lind.activiti.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProcessNodeVo {

	/**
	 * nodeId.
	 */
	private String nodeId;

	/**
	 * 节点名称.
	 */
	private String title;

	/**
	 * 节点分配角色.
	 */
	private String roleId;

	/**
	 * 节分配角色默认的用户,0表示没有默认的.
	 */
	private String defaultUserId;

	/**
	 * 是否显示驳回.
	 */
	private Integer rejectFlag;

}