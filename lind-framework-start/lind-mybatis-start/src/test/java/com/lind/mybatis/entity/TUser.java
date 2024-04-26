package com.lind.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lind.mybatis.base.BaseEntity;
import com.lind.mybatis.type.handler.ListTypeHandler;
import com.lind.mybatis.type.handler.MapTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

@Data
@TableName(value = "t_user", autoResultMap = true)
public class TUser extends BaseEntity {

	private String username;

	@TableField(typeHandler = MapTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private Map extensionInfo;

	@TableField(typeHandler = ListTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private List<String> likeList;

}
