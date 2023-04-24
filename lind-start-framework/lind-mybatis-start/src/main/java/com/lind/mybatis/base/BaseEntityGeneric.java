package com.lind.mybatis.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类.
 */
@Data
public abstract class BaseEntityGeneric implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 建立人Id,需要实现AuditorAware接口.
	 */
	@TableField(value = "create_by", fill = FieldFill.INSERT)
	@CreatedBy
	private String createBy;

	/**
	 * 建立时间.
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@CreatedDate
	private LocalDateTime createTime;

	/**
	 * 更新人ID,需要实现AuditorAware接口.
	 */
	@TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
	@LastModifiedBy
	private String updateBy;

	/**
	 * 更新时间.
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@LastModifiedDate
	private LocalDateTime updateTime;

	/**
	 * 删除标记.
	 */
	@TableField("del_flag")
	@TableLogic
	private Integer delFlag = 0;

}
