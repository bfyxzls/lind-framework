package com.lind.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lind
 * @date 2023/5/24 9:15
 * @since 1.0.0
 */
@Data
@TableName("t_config")
public class Config {

	@TableId
	Long id;

	String title;

	String code;

}
