package com.lind.common.screw;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.ArrayList;

/**
 * 生成数据库文档
 *
 * @author lind
 * @date 2023/12/18 9:58
 * @since 1.0.0
 */
public class generateDbDoc {

	static String fileOutputDir = "d:\\doc\\";

	/**
	 * 生成律师后台表结构
	 */
	@Test
	public void genereateLawyerMgr() {
		documentGeneration("jdbc:mysql://192.168.4.26:3306/keycloak", "keycloak", "root", "123456");
	}

	/**
	 * 文档生成
	 */
	void documentGeneration(String dbUrl, String fileName, String username, String password) {
		// 数据源
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariConfig.setJdbcUrl(dbUrl);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		// 设置可以获取tables remarks信息
		hikariConfig.addDataSourceProperty("useInformationSchema", "true");
		hikariConfig.setMinimumIdle(2);
		hikariConfig.setMaximumPoolSize(5);
		DataSource dataSource = new HikariDataSource(hikariConfig);
		// 生成配置
		EngineConfig engineConfig = EngineConfig.builder()
				// 生成文件路径
				.fileOutputDir(fileOutputDir)
				// 打开目录
				.openOutputDir(true)
				// 文件类型
				.fileType(EngineFileType.HTML)
				// 生成模板实现
				.produceType(EngineTemplateType.freemarker)
				// 自定义文件名称
				.fileName(fileName).build();

		// 忽略表
		ArrayList<String> ignoreTableName = new ArrayList<>();
		ignoreTableName.add("test_user");
		ignoreTableName.add("admin_event_entity");
		ignoreTableName.add("associated_policy");
		ignoreTableName.add("component");
		ignoreTableName.add("composite_role");
		ignoreTableName.add("event_entity");
		ignoreTableName.add("jgroupsping");
		ignoreTableName.add("protocol_mapper");
		ignoreTableName.add("redirect_uris");
		ignoreTableName.add("identity_provider");
		ignoreTableName.add("broker_link");
		ignoreTableName.add("identity_provider_mapper");
		ignoreTableName.add("migration_model");
		ignoreTableName.add("required_action_provider");
		ignoreTableName.add("user_consent");
		ignoreTableName.add("scope_policy");

		// 忽略表前缀
		ArrayList<String> ignorePrefix = new ArrayList<>();
		ignorePrefix.add("test_");
		ignorePrefix.add("offline_");
		ignorePrefix.add("databasechangelog");
		ignorePrefix.add("resource_");
		ignorePrefix.add("authenticator_");
		ignorePrefix.add("fed_user_");
		ignorePrefix.add("authentication_");
		ignorePrefix.add("sys_");
		ignorePrefix.add("gen_");
		// 忽略表后缀
		ArrayList<String> ignoreSuffix = new ArrayList<>();
		ignoreSuffix.add("_config");
		ignoreSuffix.add("_scope");
		ProcessConfig processConfig = ProcessConfig.builder()
				// 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
				// 根据名称指定表生成
				.designatedTableName(new ArrayList<>())
				// 根据表前缀生成
				.designatedTablePrefix(new ArrayList<>())
				// 根据表后缀生成
				.designatedTableSuffix(new ArrayList<>())
				// 忽略表名
				.ignoreTableName(ignoreTableName)
				// 忽略表前缀
				.ignoreTablePrefix(ignorePrefix)
				// 忽略表后缀
				.ignoreTableSuffix(ignoreSuffix).build();
		// 配置
		Configuration config = Configuration.builder()
				// 版本
				.version("1.0.0")
				// 描述
				.description("数据库设计文档生成")
				// 数据源
				.dataSource(dataSource)
				// 生成配置
				.engineConfig(engineConfig)
				// 生成配置
				.produceConfig(processConfig).build();
		// 执行生成
		new DocumentationExecute(config).execute();
	}

}
