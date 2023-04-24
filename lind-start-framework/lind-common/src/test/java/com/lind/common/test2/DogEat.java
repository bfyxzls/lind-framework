package com.lind.common.test2;

import com.lind.common.test.Eat;
import org.springframework.stereotype.Component;

@Component
public class DogEat implements Eat {

	@Override
	public void drink() {
		System.out.println("DogEat");
	}

}
