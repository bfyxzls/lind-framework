package com.lind.elasticsearch.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author lind
 * @date 2024/5/7 8:44
 * @since 1.0.0
 */
@Data
@Document(indexName = "edit-backend-prod")
public class LogDto {

	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	@Field(type = FieldType.Text)
	private String application;

}
