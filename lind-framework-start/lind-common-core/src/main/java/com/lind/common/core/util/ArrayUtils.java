package com.lind.common.core.util;

import java.lang.reflect.Array;

public class ArrayUtils {

	public static final int INDEX_NOT_FOUND = -1;

	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	public static boolean isEmpty(final Object[] array) {
		return getLength(array) == 0;
	}

	public static boolean isNotEmpty(final Object[] array) {
		return !isEmpty(array);
	}

	public static boolean contains(final Object[] array, final Object objectToFind) {
		return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
	}

	public static int indexOf(final Object[] array, final Object objectToFind) {
		if (isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		for (int i = 0; i < array.length; i++) {
			if ((objectToFind == null && array[i] == null) || (objectToFind != null && objectToFind.equals(array[i]))) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

}
