package com.lind.codegen;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 简易版的代码生成工具，根据vm模板生成.
 *
 * @author lind
 * @date 2024/5/28 9:28
 * @since 1.0.0
 */
public class GenDbTableEntity {

	private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final String DB_URL = "jdbc:mysql://192.168.4.26:3306/keycloak";

	private static final String USER = "root";

	private static final String PASS = "123456";
	static List<String> tableList = new ArrayList<String>() {

	};

	private static String PACKAGE_NAME = "com.demo.domain.entity";

	public static void generateDaoFromTable(ZipOutputStream zip, String daoPackage, String entityPackage,
			String tableName) throws IOException {
		VelocityEngine velocityEngine = new VelocityEngine();
		Properties prop = new Properties();
		// 可正常加载resources目录下的资源文件
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init(prop);
		Template template = velocityEngine.getTemplate("jpa/data_dao_template.vm");
		VelocityContext context = new VelocityContext();
		context.put("package", daoPackage);
		context.put("entityPackage", entityPackage);
		// 设置模板中需要的变量值
		String className = tableToJava(tableName, "");
		context.put("entityName", className);
		context.put("className", className + "Repository");
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		System.out.println(writer.toString());
		zip.putNextEntry(new ZipEntry(className + "Repository.java"));
		IoUtil.write(zip, StandardCharsets.UTF_8, false, writer.toString());
		IoUtil.close(writer);
		zip.closeEntry();
	}

	public static void generateEntityFromTable(ZipOutputStream zip, String tableName, Boolean isEntity, String suffix) {
		try {
			VelocityEngine velocityEngine = new VelocityEngine();
			Properties prop = new Properties();
			// 可正常加载resources目录下的资源文件
			prop.put("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			velocityEngine.init(prop);
			Template template = velocityEngine.getTemplate("jpa/data_dto_template.vm");
			if (isEntity) {
				template = velocityEngine.getTemplate("jpa/data_entity_template.vm");
			}
			VelocityContext context = new VelocityContext();
			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			DatabaseMetaData metaData = conn.getMetaData();
			ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
			Set<String> primaryKeys = new HashSet<>();
			while (primaryKeyResultSet.next()) {
				String columnName = primaryKeyResultSet.getString("COLUMN_NAME");
				primaryKeys.add(columnName);
			}

			ResultSet columns = metaData.getColumns(null, null, tableName, null);
			context.put("package", PACKAGE_NAME);
			// 设置模板中需要的变量值
			String className = tableToJava(tableName, "");
			if (suffix != null) {
				className = className + suffix;
			}
			context.put("className", className);
			context.put("tableName", tableName);
			List<String> fields = new ArrayList<>();
			while (columns.next()) {
				String columnName = columns.getString("COLUMN_NAME");
				String columnType = columns.getString("TYPE_NAME");
				String comment = columns.getString("REMARKS");
				StringBuffer sb = new StringBuffer();
				sb.append("\n	/**\n");
				sb.append("	 * " + comment + "\n");
				sb.append("	 */");
				if (isEntity) {
					sb.append("\n	@Column(name = \"" + columnName + "\")\n");
					if (primaryKeys.contains(columnName)) {
						sb.append("	@Id");
					}
				}
				sb.append("\n	private " + convertMySQLTypeToJavaType(columnType) + " "
						+ StringUtils.uncapitalize(columnToJava(columnName)) + ";");
				fields.add(sb.toString());
			}
			context.put("columns", fields);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			zip.putNextEntry(new ZipEntry(className + ".java"));
			IoUtil.write(zip, StandardCharsets.UTF_8, false, writer.toString());
			IoUtil.close(writer);
			zip.closeEntry();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 */
	private static String tableToJava(String tableName, String tablePrefix) {
		if (StringUtils.isNotBlank(tablePrefix)) {
			tableName = tableName.replaceFirst(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	public static String convertMySQLTypeToJavaType(String mysqlType) {
		Map<String, String> typeMap = new HashMap<>();
		// 添加MySQL数据库类型与Java数据类型的对应关系
		typeMap.put("TINYINT", "byte");
		typeMap.put("SMALLINT", "short");
		typeMap.put("MEDIUMINT", "int");
		typeMap.put("INT", "int");
		typeMap.put("BIGINT", "long");
		typeMap.put("FLOAT", "float");
		typeMap.put("DOUBLE", "double");
		typeMap.put("DECIMAL", "BigDecimal");
		typeMap.put("CHAR", "String");
		typeMap.put("VARCHAR", "String");
		typeMap.put("TEXT", "String");
		typeMap.put("BLOB", "byte[]");
		typeMap.put("DATE", "java.time.LocalDate");
		typeMap.put("TIME", "java.time.LocalTime");
		typeMap.put("DATETIME", "java.time.LocalDateTime");
		typeMap.put("TIMESTAMP", "java.sql.Timestamp");
		typeMap.put("BIT", "boolean");
		return typeMap.get(mysqlType.toUpperCase());
	}

	@BeforeEach
	void init() {
		tableList = new ArrayList<String>();
		tableList.add("table_user_account");
		tableList.add("table_user_account_asset_rel");
		tableList.add("table_user_account_device_rel");
		tableList.add("table_user_platform");
		tableList.add("table_user_platform_account_rel");
		tableList.add("table_user_platform_ip_rel");
		tableList.add("table_user_platform_user_admin_rel");
		tableList.add("table_user_user");
		tableList.add("table_user_user_account_rel");
	}

	/**
	 * 生成实体.
	 */
	@Test
	public void genEntity() {
		String zipFilePath = "d:/entity.zip"; // 压缩后的zip文件路径
		try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zip = new ZipOutputStream(fos)) {
			tableList.forEach(o -> {
				generateEntityFromTable(zip, o, true, null);
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成DTO.
	 */
	@Test
	public void genDTO() {
		String zipFilePath = "d:/dto.zip"; // 压缩后的zip文件路径
		try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zip = new ZipOutputStream(fos)) {
			tableList.forEach(o -> {
				generateEntityFromTable(zip, o, false, "DTO");
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成JPA的REPO
	 */
	@Test
	public void genDao() {
		String zipFilePath = "d:/dao.zip"; // 压缩后的zip文件路径
		try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zip = new ZipOutputStream(fos)) {
			tableList.forEach(o -> {
				try {
					generateDaoFromTable(zip, "com.demo.dao.mysql", "com.demo.domain.entity", o);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
