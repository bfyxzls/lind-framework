package com.lind.mybatis.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Exrickx
 */
@Data
public abstract class BaseEntityInt extends BaseEntityGeneric {

	@TableId(type = IdType.AUTO)
	private Integer id;

}
