package com.lind.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lind.mybatis.base.BaseEntity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@TableName("t_user")
public class TUser extends BaseEntity {

	private String username;

}
