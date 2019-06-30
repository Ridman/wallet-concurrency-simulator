package ru.undefined.simulator.server.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource({"classpath:application.properties", "classpath:application-test.properties"})
public class AccountServiceIT {

}
