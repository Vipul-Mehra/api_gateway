package com.paxaris.productManagement;

import jakarta.transaction.Transactional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@Transactional
public class ProjectManager {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManager.class, args);

	}

}
