package com.lind.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lind.elasticsearch.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 回复
 */
@Data
@Document(indexName = "pkulaw_reply")
@ToString(callSuper = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends EsBaseEntity {

	/**
	 * 文章标识
	 */
	@Field(type = FieldType.Keyword)
	private String articleId;

	/**
	 * 段落标识,可以为空
	 */
	@Field(type = FieldType.Keyword)
	private String paragraphId;

	/**
	 * 评论ID
	 */
	@Field(type = FieldType.Keyword)
	private String commentId;

	/**
	 * 发起人
	 */
	@Field(type = FieldType.Keyword)
	private String fromUserId;

	/**
	 * 目标人
	 */
	@Field(type = FieldType.Keyword)
	private String toUserId;

	/**
	 * 内容
	 */
	@Field(type = FieldType.Text)
	private String content;

	/**
	 * 时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = dateTimeFormat)
	private Date replyTime;

	/**
	 * 状态：0初始化，1正常
	 */
	@Field(type = FieldType.Keyword)
	private Status status;

}
