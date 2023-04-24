package com.lind.hbase.util;

import org.apache.commons.lang3.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author:-J-
 * @date:2020/8/31
 * @description
 */
public class SerializationUtils {

	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

			try {
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				Throwable var3 = null;

				try {
					oos.writeObject(object);
					oos.flush();
				}
				catch (Throwable var13) {
					var3 = var13;
					throw var13;
				}
				finally {
					if (oos != null) {
						if (var3 != null) {
							try {
								oos.close();
							}
							catch (Throwable var12) {
								var3.addSuppressed(var12);
							}
						}
						else {
							oos.close();
						}
					}

				}
			}
			catch (IOException var15) {
				throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), var15);
			}

			return baos.toByteArray();
		}
	}

	public static String deserialize(InputStream inputStream) {
		if (inputStream == null) {
			throw new IllegalArgumentException("The InputStream must not be null");
		}
		else {
			ObjectInputStream in = null;

			String var3;
			try {
				in = new ObjectInputStream(inputStream);
				String obj = in.readObject().toString();
				var3 = obj;
			}
			catch (ClassNotFoundException var13) {
				throw new SerializationException(var13);
			}
			catch (IOException var14) {
				throw new SerializationException(var14);
			}
			finally {
				try {
					if (in != null) {
						in.close();
					}
				}
				catch (IOException var12) {
				}

			}

			return var3;
		}
	}

	public static String deserialize(byte[] objectData, boolean flag) {
		Object o = deserialize(objectData);
		if (o != null) {
			return o.toString();
		}
		else {
			return "";
		}
	}

	public static Object deserialize(byte[] bytes) {

		if (bytes == null || isEmptySerialize(bytes)) {
			return null;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			return ois.readObject();
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Failed to deserialize object", ex);
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Failed to deserialize object type", ex);
		}
	}

	private static boolean isEmptySerialize(byte[] bytes) {
		byte[] empty = serialize("");
		if (bytes.length == 0 || empty.length == bytes.length) {
			return true;
		}

		for (int i = 0; i < empty.length; i++) {
			if (empty[i] != bytes[i]) {
				return false;
			}
		}
		return true;
	}

}
