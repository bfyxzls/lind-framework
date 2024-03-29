package com.lind.activiti.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 节点配置表.
 */
@Table(name = "act_re_node")
@Entity
@ApiModel("节点配置")
public class ActReNode {

	@Id
	private String id;

	private Date createTime;

	private Date updateTime;

	private String createBy;

	private String updateBy;

	/**
	 * 流程定义.
	 */
	private String processDefId;

	/**
	 * 节点.
	 */
	private String nodeId;

	/**
	 * 审核角色ID.
	 */
	private String roleId;

	/**
	 * 默认审核用户ID.
	 */
	private String defaultUserId;

	private String tenantId;

	private Integer delFlag;

	/**
	 * 是否有驳回按钮.
	 */
	private Integer rejectFlag;

	public Integer getRejectFlag() {
		return rejectFlag;
	}

	public void setRejectFlag(Integer rejectFlag) {
		this.rejectFlag = rejectFlag;
	}

	public String getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getDefaultUserId() {
		return defaultUserId;
	}

	public void setDefaultUserId(String defaultUserId) {
		this.defaultUserId = defaultUserId;
	}

}
