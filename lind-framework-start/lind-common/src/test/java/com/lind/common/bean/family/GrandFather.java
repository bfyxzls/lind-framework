package com.lind.common.bean.family;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2022/8/18 11:07
 * @since 1.0.0
 */
@Component
@AutoConfigureBefore(Father.class) // 在我儿子Father之前，我GrandFather先初始化
public class GrandFather {

	public GrandFather() {
		System.out.println("配置類GrandFatherConfig構造器被執行...");
	}

}
