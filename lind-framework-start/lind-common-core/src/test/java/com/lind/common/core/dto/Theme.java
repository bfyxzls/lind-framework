package com.lind.common.core.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lind
 * @date 2023/7/14 16:18
 * @since 1.0.0
 */
@Data
public class Theme {

	private List<ThemeRepresentation> themes;

	public List<ThemeRepresentation> getThemes() {
		return themes;
	}

	public void setThemes(List<ThemeRepresentation> themes) {
		this.themes = themes;
	}

}
