package com.lind.common.bean.postprocessor;

import java.util.HashMap;
import java.util.Map;

public class Context {

	Map<String, AbstractSend> dic = new HashMap<>();

	public void print() {
		dic.forEach((i, o) -> System.out.println(i));
	}

}
