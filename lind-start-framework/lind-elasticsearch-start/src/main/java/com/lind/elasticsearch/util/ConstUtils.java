package com.lind.elasticsearch.util;

import com.google.common.collect.Sets;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

public interface ConstUtils {

	/**
	 * 非nested类型能用的字段
	 */
	Set<FieldType> NON_INSIDE_FIELD_TYPE = Sets.newHashSet(FieldType.Text, FieldType.Nested, FieldType.Object);

	String TIME_FORMATTER = "yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM"
			+ ".dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy.MM.dd HH:mm:ss||yyyy/MM/dd "
			+ "HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd HH:mm:ss.SSS";

	String UPDATE_USER = "updateUser";

	String UPDATE_TIME = "updateTime";

	String CREATE_USER = "createUser";

	String CREATE_TIME = "createTime";

}
