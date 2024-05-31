package com.lind.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

/**
 * 注意：在test项目里，当没有添加@RunWith(SpringRunner.class)时，通过insert插入实体时建立的索引是有问题的（解决方法是手动运行createIndex和putMapping），
 */
@Data
@Document(indexName = "esdto")
@Setting(shards = 1, replicas = 0) // @Setting里的配置会覆盖@Document里的配置
public class EsDto extends EsBaseEntity implements Serializable {

	/**
	 * keyword类型不自动分词；text类型会自动分词.
	 */
	@Field(type = FieldType.Keyword)
	private String name;

	@Field(type = FieldType.Integer)
	private Integer age;

	@Field(type = FieldType.Integer)
	private Integer sex;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String desc;

	@Field(type = FieldType.Text, analyzer = "standard")
	private String memo;

	@Field(type = FieldType.Nested, fielddata = true)
	private Address address;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Address {

		@Field(type = FieldType.Keyword)
		private String province;

		@Field(type = FieldType.Keyword)
		private String city;

		@Field(type = FieldType.Keyword)
		private String district;

	}

	@Field(type = FieldType.Keyword)
	private String email;

}
