package com.lind.mybatis.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

/**
 * @author Exrickx
 */
@Data
@ToString(callSuper = true)
public abstract class BaseEntity extends BaseEntityGeneric {

	/**
	 * 主键. final保证了不会被其它实体override,例如由vo向entity赋值时,需要使用final来控制一下.
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String id;

}
