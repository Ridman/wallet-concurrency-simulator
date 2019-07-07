package ru.undefined.simulator.server.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"ru.undefined.simulator.commons.model"})
@EnableJpaRepositories(basePackages = {"ru.undefined.simulator.server.repository"})
public class JPAConfig {
}
