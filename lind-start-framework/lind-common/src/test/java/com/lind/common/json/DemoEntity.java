package com.lind.common.json;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author lind
 * @date 2023/2/8 10:18
 * @since 1.0.0
 */
@Data
@Builder
public class DemoEntity {

	private String title;

	private Integer count;

	private List<DemoEntity> sons;

}
