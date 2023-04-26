package com.lind.common.proxy;

import com.lind.common.proxy.annoproxy.DictionaryAdapterField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@DictionaryAdapterField
	private String name;

}
