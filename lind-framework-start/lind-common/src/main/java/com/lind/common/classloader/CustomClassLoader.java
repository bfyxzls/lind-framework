package com.lind.common.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class loader shall load classes from a custom location which is not part of the
 * class path.
 * <p>
 * In real time scenario this location can be a network file system, or a socket or an
 * encrypted class.
 *
 * @author ourownjava.com
 */
public class CustomClassLoader extends ClassLoader {

	private static final String CLASS_FILE_LOCATION = "D:\\github\\lind-start\\lind-start-framework\\lind-common\\target\\classes\\";

	/**
	 * Override the loadClass method and delegate the responsibility to load the class to
	 * it's super class. If the parent class loader is not able to load the class then
	 * this class loader shall try to load the class by itself.
	 */
	@Override
	public Class<?> loadClass(final String name) {
		Class<?> clazz = null;
		try {
			clazz = super.loadClass(name);
		}
		catch (final ClassNotFoundException e) {
			// This means the parent class is not able to find the class.
			// not in <JAVA_HOME>/lib, <JAVA_HOME>/lib/ext, -cp, -classpath
			// try to load the class using our own custom class loader
			e.printStackTrace();
		}
		if (null == clazz) {
			try {
				final byte[] clazzByte = loadClassByte(name);
				clazz = defineClass(name, clazzByte, 0, clazzByte.length);
				resolveClass(clazz);
			}
			catch (final IOException e) {
				// this means the given class is not found the custom location
				e.printStackTrace();
			}
		}
		return clazz;
	}

	/**
	 * This method shall take the class name and load the class file.
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private byte[] loadClassByte(final String name) throws IOException {
		// convert the give class name to file format like
		// /home/thosan/java/clazzez/sanju/org/corejava/util/ClassDoesNotExistInClassPath
		final StringBuilder fileNameBuilder = new StringBuilder(CLASS_FILE_LOCATION);
		fileNameBuilder.append(name.replace('.', File.separatorChar));
		fileNameBuilder.append(".class");
		// read the file and grab the byte stream
		byte[] data = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(fileNameBuilder.toString()));
			data = new byte[inputStream.available()];
			inputStream.read(data);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException(e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage(), e);
		}
		finally {
			if (null != inputStream) {
				inputStream.close();
			}
		}
		return data;
	}

}
