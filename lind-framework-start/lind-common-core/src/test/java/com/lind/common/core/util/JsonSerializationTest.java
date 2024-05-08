package com.lind.common.core.util;

import com.lind.common.core.dto.Theme;
import com.lind.common.core.dto.ThemeRepresentation;
import com.lind.common.core.jackson.serialization.JsonSerialization;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class JsonSerializationTest {

	public static final String KEYCLOAK_THEMES_JSON = "keycloak-themes.json";

	private ThemeRepresentation[] themes;

	public ThemeRepresentation[] getThemes() {
		return themes;
	}

	public void setThemes(ThemeRepresentation[] themes) {
		this.themes = themes;
	}

	@Test
	public void readJson() throws IOException {
		InputStream themesInputStream = this.getClass().getClassLoader().getResourceAsStream(KEYCLOAK_THEMES_JSON);
		Theme themesRepresentation = JsonSerialization.readValue(themesInputStream, Theme.class);
		for (ThemeRepresentation item : themesRepresentation.getThemes()) {
			log.info("ThemeRepresentation.name:{},types:{}", item.getName(), item.getTypes());
		}
	}

}
