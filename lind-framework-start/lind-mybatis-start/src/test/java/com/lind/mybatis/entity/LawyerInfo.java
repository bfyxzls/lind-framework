package com.lind.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@TableName("lawyer_info")
public class LawyerInfo {

	@TableId(type = IdType.AUTO)
	private long gid;

	private String lawyerName;

}
