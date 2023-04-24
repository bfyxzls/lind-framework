package com.lind.common.util;

import com.lind.common.jackson.JsonSerialization;
import com.lind.common.theme.ThemesRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class JsonSerializationTest {

	public static final String KEYCLOAK_THEMES_JSON = "META-INF/keycloak-themes.json";

	@Test
	public void readJson() throws IOException {
		InputStream themesInputStream = this.getClass().getClassLoader().getResourceAsStream(KEYCLOAK_THEMES_JSON);
		ThemesRepresentation themesRepresentation = JsonSerialization.readValue(themesInputStream,
				ThemesRepresentation.class);
		for (ThemesRepresentation.ThemeRepresentation item : themesRepresentation.getThemes()) {
			log.info("ThemeRepresentation.name:{},types:{}", item.getName(), item.getTypes());
		}
	}

}
