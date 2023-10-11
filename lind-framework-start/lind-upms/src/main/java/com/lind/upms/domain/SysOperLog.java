package com.lind.upms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lind.upms.annotation.Excel;
import com.lind.upms.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 操作日志记录对象 sys_oper_log
 *
 * @author ruoyi
 * @date 2023-08-27
 */
@ApiModel("操作日志记录")
public class SysOperLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 日志主键 */
	private Long operId;

	/** 模块标题 */
	@Excel(name = "模块标题")
	@ApiModelProperty(value = "模块标题")
	private String title;

	/** 业务类型（0其它 1新增 2修改 3删除） */
	@Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除")
	@ApiModelProperty(value = "业务类型")
	private Integer businessType;

	/** 方法名称 */
	@Excel(name = "方法名称")
	@ApiModelProperty(value = "方法名称")
	private String method;

	/** 请求方式 */
	@Excel(name = "请求方式")
	@ApiModelProperty(value = "请求方式")
	private String requestMethod;

	/** 操作类别（0其它 1后台用户 2手机端用户） */
	@Excel(name = "操作类别", readConverterExp = "0=其它,1=后台用户,2=手机端用户")
	@ApiModelProperty(value = "操作类别")
	private Integer operatorType;

	/** 操作人员 */
	@Excel(name = "操作人员")
	@ApiModelProperty(value = "操作人员")
	private String operName;

	/** 部门名称 */
	@Excel(name = "部门名称")
	@ApiModelProperty(value = "部门名称")
	private String deptName;

	/** 请求URL */
	@Excel(name = "请求URL")
	@ApiModelProperty(value = "请求URL")
	private String operUrl;

	/** 主机地址 */
	@Excel(name = "主机地址")
	@ApiModelProperty(value = "主机地址")
	private String operIp;

	/** 操作地点 */
	@Excel(name = "操作地点")
	@ApiModelProperty(value = "操作地点")
	private String operLocation;

	/** 请求参数 */
	@Excel(name = "请求参数")
	@ApiModelProperty(value = "请求参数")
	private String operParam;

	/** 返回参数 */
	@Excel(name = "返回参数")
	@ApiModelProperty(value = "返回参数")
	private String jsonResult;

	/** 操作状态（0正常 1异常） */
	@Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
	@ApiModelProperty(value = "操作状态")
	private Integer status;

	/** 错误消息 */
	@Excel(name = "错误消息")
	@ApiModelProperty(value = "错误消息")
	private String errorMsg;

	/** 操作时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "操作时间")
	private Date operTime;

	/** 消耗时间 */
	@Excel(name = "消耗时间")
	@ApiModelProperty(value = "消耗时间")
	private Long costTime;

	/** 业务主键 */
	@Excel(name = "业务主键")
	@ApiModelProperty(value = "业务主键")
	private String businessId;

	/** 业务变更内容 */
	@Excel(name = "业务变更内容")
	@ApiModelProperty(value = "业务变更内容")
	private String businessInfo;

	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	public Long getOperId() {
		return operId;
	}

	public void setOperId(Long operId) {
		this.operId = operId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Integer getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(Integer operatorType) {
		this.operatorType = operatorType;
	}

	public String getOperName() {
		return operName;
	}

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getOperUrl() {
		return operUrl;
	}

	public void setOperUrl(String operUrl) {
		this.operUrl = operUrl;
	}

	public String getOperIp() {
		return operIp;
	}

	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}

	public String getOperLocation() {
		return operLocation;
	}

	public void setOperLocation(String operLocation) {
		this.operLocation = operLocation;
	}

	public String getOperParam() {
		return operParam;
	}

	public void setOperParam(String operParam) {
		this.operParam = operParam;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public Long getCostTime() {
		return costTime;
	}

	public void setCostTime(Long costTime) {
		this.costTime = costTime;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("operId", getOperId())
				.append("title", getTitle()).append("businessType", getBusinessType()).append("method", getMethod())
				.append("requestMethod", getRequestMethod()).append("operatorType", getOperatorType())
				.append("operName", getOperName()).append("deptName", getDeptName()).append("operUrl", getOperUrl())
				.append("operIp", getOperIp()).append("operLocation", getOperLocation())
				.append("operParam", getOperParam()).append("jsonResult", getJsonResult()).append("status", getStatus())
				.append("errorMsg", getErrorMsg()).append("operTime", getOperTime()).append("costTime", getCostTime())
				.append("businessId", getBusinessId()).toString();
	}

}
