package ru.undefined.simulator.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.undefined.simulator"})
public class WalletServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletServerApplication.class, args);
    }
}
