package com.lind.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lind.elasticsearch.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 标签
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentLabel {

	/**
	 * 评论内容
	 */
	@Field(type = FieldType.Keyword)
	private String title;

	/**
	 * 发表回复的用户id
	 */
	@Field(type = FieldType.Keyword)
	private String fromUserId;

	/**
	 * 评论时间@JsonFormat在由json到es时序列化时起到作用
	 */
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "GMT+8")
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = EsBaseEntity.dateTimeFormat)
	private Date occurTime;

	/**
	 * 状态：0初始化，1正常
	 */
	@Field(type = FieldType.Keyword)
	private Status status;

}
