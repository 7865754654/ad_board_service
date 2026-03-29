package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//Почиттаь про компанент скан
@ComponentScan(basePackages = {"org.example", "service", "repository", "mapper", "dto", "entity"})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}