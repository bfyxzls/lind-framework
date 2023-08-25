package com.lind.rbac;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@MapperScan("com.lind.rbac.dao")
public class RbacApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext application = SpringApplication.run(RbacApplication.class, args);
		Environment env = application.getEnvironment();
		log.info(
				"\n----------------------------------------------------------\n\t"
						+ "Application '{}' is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:{}/login\n\t"
						+ "Home: \t\thttp://localhost:{}/\n\t" + "Doc: \thttp://localhost:{}/doc.html\n"
						+ "----------------------------------------------------------",
				env.getProperty("spring.application.name"), env.getProperty("server.port"),
				env.getProperty("server.port"));
	}

}
