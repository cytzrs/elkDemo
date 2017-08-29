package com.elastic.stack.demo.elkDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ElkDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElkDemoApplication.class, args);
	}

}
