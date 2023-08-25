/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.common.core.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lind.common.core.jackson.module.DefaultValueModule;
import com.lind.common.core.jackson.module.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * json响应配置. Long到字符串，避免Long被截取 Date日期格式化 LocalDate日期格式化
 *
 * @author lengleng
 * @author L.cm
 * @author lishangbu
 * @date 2020-06-17
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			// 早期的Date类型的格式化，默认的返回日期2022-04-21T10:28:00，添加后返回日期2022-04-21 10:28:07
			builder.simpleDateFormat(JavaTimeModule.NORM_DATETIME_PATTERN);
			// java8建议的LocalDate,LocalDateTime,LocalTime的格式化
			builder.modules(new JavaTimeModule());
			// 默认值的处理
			builder.modules(new DefaultValueModule());
			// Long 类型转 String 类型,避免js丢失精度
			builder.serializerByType(Long.class, ToStringSerializer.instance);
		};
	}

}
