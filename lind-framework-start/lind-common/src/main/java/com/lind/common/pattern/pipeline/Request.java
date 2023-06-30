package com.lind.common.pattern.pipeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lind
 * @date 2023/6/28 17:43
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Request {

	private String ip;

	private String userId;

}
