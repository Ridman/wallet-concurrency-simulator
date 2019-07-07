package ru.undefined.simulator.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import ru.undefined.simulator.client.service.LoadGenerator;

@ComponentScan(basePackages = {"ru.undefined.simulator"})
@SpringBootApplication
public class WalletClientApplication implements CommandLineRunner {

    @Autowired
    private LoadGenerator loadGenerator;

    public static void main(String[] args) {
        new SpringApplicationBuilder(WalletClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        loadGenerator.generate();
    }
}
