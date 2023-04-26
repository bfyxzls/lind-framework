package com.lind.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LombokTest {

	@Test
	public void supperSubEqual() {
		Man man = new Man();
		man.setName("zzl");
		man.setHunting(true);
		Man man1 = new Man();
		man1.setHunting(true);
		man1.setName("lind");
		log.info("man==man1 ? {}", man.equals(man1)); // false

		Woman woman = new Woman();
		woman.setName("zzl");
		woman.setSpin(true);
		Woman woman1 = new Woman();
		woman1.setSpin(true);
		woman1.setName("lind");
		log.info("woman==woman1 ? {}", woman.equals(woman1)); // true
	}

	@Data
	class Person {

		String name;

	}

	@Data
	// 重写时带上父类字段
	@EqualsAndHashCode(callSuper = true)
	class Man extends Person {

		Boolean hunting;

	}

	@Data
	// 重写equals时不会带上父类的字段，同种类型比较时，当子类字段相同时，结果就为true，这显然是不准确的.
	@EqualsAndHashCode(callSuper = false)
	class Woman extends Person {

		Boolean spin;

	}

}
