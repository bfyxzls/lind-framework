package com.lind.common.shallowdeep;

import java.io.Serializable;

/**
 * @author lind
 * @date 2023/9/11 14:16
 * @since 1.0.0
 */

public class Address implements Serializable {

	private String city;

	public Address(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
