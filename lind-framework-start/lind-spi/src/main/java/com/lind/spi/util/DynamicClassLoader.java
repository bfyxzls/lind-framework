package com.lind.spi.util;

/*
 * Copyright 2018 Mordechai Meisels
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * 动态类加载器. 将外部jar包装载到当前类加载器里，支持热加载.
 */
public final class DynamicClassLoader extends URLClassLoader {

	static {
		// 并行加载
		registerAsParallelCapable();
	}

	public DynamicClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public static DynamicClassLoader findAncestor(ClassLoader cl) {
		do {

			if (cl instanceof DynamicClassLoader)
				return (DynamicClassLoader) cl;

			cl = cl.getParent();
		}
		while (cl != null);

		return null;
	}

	void add(URL url) {
		addURL(url);
	}

	/*
	 * Required for Java Agents when this classloader is used as the system classloader
	 */
	@SuppressWarnings("unused")
	private void appendToClassPathForInstrumentation(String jarfile) throws IOException {
		add(Paths.get(jarfile).toRealPath().toUri().toURL());
	}

}
