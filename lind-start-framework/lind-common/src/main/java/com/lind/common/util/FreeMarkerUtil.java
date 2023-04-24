/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.common.util;

import freemarker.cache.URLTemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class FreeMarkerUtil {

	static String root = "templates/";

	/**
	 * 模板渲染
	 * @param data 数据字典
	 * @param templateName 模块名
	 * @param theme 皮肤名
	 * @return
	 */
	@SneakyThrows
	public String processTemplate(Object data, String templateName, String theme) {
		if (data instanceof Map) {
			((Map) data).put("kcSanitize", new SanitizerMethod());
		}

		try {
			Template template = getTemplate(templateName, theme);
			Writer out = new StringWriter();
			template.process(data, out);
			return out.toString();
		}
		catch (Exception e) {
			throw e;
		}
	}

	private Template getTemplate(String templateName, String theme) throws IOException {
		Configuration cfg = new Configuration();

		// Assume *.ftl files are html. This lets freemarker know how to
		// sanitize and prevent XSS attacks.
		if (templateName.toLowerCase().endsWith(".ftl")) {
			cfg.setOutputFormat(HTMLOutputFormat.INSTANCE);
		}

		cfg.setTemplateLoader(new ThemeTemplateLoader(theme));
		return cfg.getTemplate(templateName, "UTF-8");
	}

	static class ThemeTemplateLoader extends URLTemplateLoader {

		private String theme;

		public ThemeTemplateLoader(String theme) {
			this.theme = theme;
		}

		@Override
		protected URL getURL(String name) {
			String path = Paths.get(root, name).toString();
			if (theme != null) {
				path = Paths.get(root, theme, name).toString();
			}
			URL file = this.getClass().getClassLoader().getResource(path);
			return file;
		}

	}

	/**
	 * Allows sanitizing of html that uses Freemarker ?no_esc. This way, html can be
	 * allowed but it is still cleaned up for safety. Tags and attributes deemed unsafe
	 * will be stripped out.
	 */
	public static class SanitizerMethod implements TemplateMethodModelEx {

		@Override
		public Object exec(List list) throws TemplateModelException {
			if ((list.isEmpty()) || (list.get(0) == null)) {
				throw new NullPointerException("Can not escape null value.");
			}

			String html = list.get(0).toString();

			return fixURLs(html);
		}

		private String fixURLs(String msg) {
			Pattern hrefs = Pattern.compile("href=\"([^\"]*)\"");
			Matcher matcher = hrefs.matcher(msg);
			int count = 0;
			while (matcher.find()) {
				count++;
				String original = matcher.group(count);
				String href = original.replaceAll("&#61;", "=").replaceAll("\\.\\.", ".").replaceAll("&amp;", "&");
				msg = msg.replace(original, href);
			}
			return msg;
		}

	}

}
