package com.lind.common.core.dto;

import lombok.Data;

/**
 * @author lind
 * @date 2023/7/14 16:18
 * @since 1.0.0
 */
@Data
public class ThemeRepresentation {

	private String name;

	private String[] types;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

}
