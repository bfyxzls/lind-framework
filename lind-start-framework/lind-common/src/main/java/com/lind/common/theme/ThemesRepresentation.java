package com.lind.common.theme;

public class ThemesRepresentation {

	private ThemeRepresentation[] themes;

	public ThemeRepresentation[] getThemes() {
		return themes;
	}

	public void setThemes(ThemeRepresentation[] themes) {
		this.themes = themes;
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

}
