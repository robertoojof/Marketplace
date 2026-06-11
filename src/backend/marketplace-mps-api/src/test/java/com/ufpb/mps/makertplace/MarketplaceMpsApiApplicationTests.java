package com.ufpb.mps.makertplace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class MarketplaceMpsApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void verificaArquiteturaModular() {
		ApplicationModules modules = ApplicationModules.of(MarketplaceMpsApiApplication.class);

		System.out.println("Quantidade de modulos: " + modules.stream().count());
		modules.forEach(module -> System.out.println("Modulo: " + module.getName()));
		modules.verify();
	}
}
