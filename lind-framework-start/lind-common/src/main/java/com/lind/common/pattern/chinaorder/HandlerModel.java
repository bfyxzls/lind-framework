package com.lind.common.pattern.chinaorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lind
 * @date 2023/6/28 8:51
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HandlerModel implements Comparable<HandlerModel> {

	private String title;

	private String classPath;

	private Integer sort;

	@Override
	public int compareTo(HandlerModel o) {
		return o.getSort() - this.sort; // 降序
	}

}
