package com.lind.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyObject {

	private String title;

	public MyObject print() {
		System.out.println("hello world!");
		return this;
	}

}
