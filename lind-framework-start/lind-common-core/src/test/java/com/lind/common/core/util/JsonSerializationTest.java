package com.lind.common.core.util;

import com.lind.common.core.jackson.JsonSerialization;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

	public static class ThemeRepresentation {

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

	public class Theme {

		private List<ThemeRepresentation> themes;

		public List<ThemeRepresentation> getThemes() {
			return themes;
		}

		public void setThemes(List<ThemeRepresentation> themes) {
			this.themes = themes;
		}

	}

}
