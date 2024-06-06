package br.newton.SAA_RestAPI.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"br.newton"})
@EnableMongoRepositories("br.newton.SAA_RestAPI.repository")
public class SaaRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaaRestApiApplication.class, args);
	}

}
