package br.edu.fiec.RotaVan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // IMPORTAR
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // IMPORTAR

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.edu.fiec.RotaVan.features")
@EntityScan(basePackages = "br.edu.fiec.RotaVan.features")
public class RotaVanApplication {

	public static void main(String[] args) {
		SpringApplication.run(RotaVanApplication.class, args);
	}

}