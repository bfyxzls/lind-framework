package com.lind.redis;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lind
 * @date 2022/9/16 16:28
 * @since 1.0.0
 */
@Data
class User implements Serializable {

	private Integer id;

	private String name;

	private LocalDateTime createTime;

}
