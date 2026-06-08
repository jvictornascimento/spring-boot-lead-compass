package com.jvictornascimento.leadCompass;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class PackageStructureTests {

	private static final Path BASE_PACKAGE = Path.of("src/main/java/com/jvictornascimento/leadCompass");

	private static final List<String> DOMAINS = List.of(
			"auth",
			"dashboard",
			"searches",
			"leads",
			"diagnostics",
			"campaigns",
			"messages",
			"offers",
			"followups",
			"interactions",
			"exports",
			"settings");

	private static final List<String> DOMAIN_SUBPACKAGES = List.of(
			"controller",
			"service",
			"repository",
			"dto",
			"mapper",
			"model");

	@Test
	void domainPackagesFollowProjectStructure() {
		assertThat(Files.exists(BASE_PACKAGE)).isTrue();

		for (String domain : DOMAINS) {
			Path domainPath = BASE_PACKAGE.resolve(domain);

			assertThat(Files.exists(domainPath.resolve("package-info.java")))
					.as("domain package-info for %s", domain)
					.isTrue();

			for (String subpackage : DOMAIN_SUBPACKAGES) {
				assertThat(Files.exists(domainPath.resolve(subpackage).resolve("package-info.java")))
						.as("%s.%s package-info", domain, subpackage)
						.isTrue();
			}
		}
	}
}
