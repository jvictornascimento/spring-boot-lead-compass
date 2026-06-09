package com.jvictornascimento.leadCompass;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class PersistenceConfigurationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Flyway flyway;

	@Autowired
	private Environment environment;

	@Test
	void testProfileLoadsDatasourceAndFlyway() {
		assertThat(dataSource).isNotNull();
		assertThat(flyway).isNotNull();
		assertThat(environment.matchesProfiles("test")).isTrue();
		assertThat(environment.getProperty("spring.datasource.url")).startsWith("jdbc:h2:");
	}
}
