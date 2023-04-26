package com.lind.redis;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lind
 * @date 2022/9/16 16:28
 * @since 1.0.0
 */
@Data
class User implements Serializable {

	private Integer id;

	private String name;

}
