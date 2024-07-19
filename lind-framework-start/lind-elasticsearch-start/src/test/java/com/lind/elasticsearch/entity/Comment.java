package com.lind.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lind.elasticsearch.enums.CommentType;
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
import java.util.List;

/**
 * 评论
 */
@Data
@Document(indexName = "demo_comment")
@ToString(callSuper = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends EsBaseEntity {

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
	 * 类型：article,paragraph
	 */
	@Field(type = FieldType.Keyword)
	private CommentType commentType;

	/**
	 * 评论内容
	 */
	@Field(type = FieldType.Text)
	private String content;

	/**
	 * 发表评论的用户id
	 */
	@Field(type = FieldType.Keyword)
	private String fromUserId;

	/**
	 * 评论时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = dateTimeFormat)
	private Date commentTime;

	/**
	 * 状态：0初始化，1正常
	 */
	@Field(type = FieldType.Keyword)
	private Status status;

	/**
	 * 评论分数
	 */
	private Integer star;

	@Field(type = FieldType.Nested)
	private List<CommentLabel> replies;

	@Field(type = FieldType.Nested)
	private UserInfo userInfo;

}
