package ru.undefined.simulator.server.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.undefined.simulator.commons.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource({"classpath:application.properties", "classpath:application-test.properties"})
public class AccountServiceIT {

    @Autowired
    private AccountService accountService;

    @Test
    public void whenInsufficientFundsThenExceptionThrown() {
        User user = new User();

    }
}
