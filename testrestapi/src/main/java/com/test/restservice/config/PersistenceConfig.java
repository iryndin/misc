package com.test.restservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.test.restservice.repo"})
@EntityScan("com.test.restservice.model")
public class PersistenceConfig {
}
