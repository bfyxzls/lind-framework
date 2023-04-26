package com.lind.elasticsearch.entity;

import com.lind.common.util.SnowFlakeUtils;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * es基础实体.
 *
 * @Field注解需要手动调用createIndex()和putMapping才能初始化.
 */
@Data
public class EsBaseEntity {

	public static final String dateTimeFormat = "yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM.dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||"
			+ "yyyy.MM.dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd HH:mm:ss.SSS";

	/**
	 * 主键.
	 */
	@Id
	private final String id = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());

	/**
	 * 创建时间.
	 */
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = dateTimeFormat)
	@CreatedDate
	protected String createTime;

	/**
	 * 创建人.
	 */
	@Field(type = FieldType.Keyword)
	@CreatedBy
	protected String createUser;

	/**
	 * 更新时间.
	 */
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = dateTimeFormat)
	@LastModifiedDate
	protected String updateTime;

	/**
	 * 更新人.
	 */
	@Field(type = FieldType.Keyword)
	@LastModifiedBy
	protected String updateUser;

	/**
	 * 删除标记.
	 */
	@Field(type = FieldType.Boolean)
	protected boolean delFlag;

}
