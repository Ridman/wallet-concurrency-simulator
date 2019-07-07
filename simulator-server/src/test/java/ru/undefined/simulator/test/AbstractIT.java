package ru.undefined.simulator.test;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.User;
import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.server.Application;
import ru.undefined.simulator.server.repository.UserAccountRepository;
import ru.undefined.simulator.server.repository.UserRepository;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration
@TestPropertySource(locations = {"classpath:/application.properties", "classpath:/application-test.properties"})
public class AbstractIT {

    protected List<User> existingUsers = new LinkedList<>();

    protected List<UserAccount> existingUserAccounts = new LinkedList<>();

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    @Before
    public void setUp() {
        User u1 = new User();
        u1.setId(1L);
        existingUsers.add(u1);

        User u2 = new User();
        u2.setId(2L);
        existingUsers.add(u2);

        userRepository.saveAll(existingUsers);

        existingUserAccounts.add(userAccount(u1.getId(), Currency.EUR));
        existingUserAccounts.add(userAccount(u1.getId(), Currency.USD));
        existingUserAccounts.add(userAccount(u1.getId(), Currency.GPB));

        existingUserAccounts.add(userAccount(u2.getId(), Currency.EUR));
        existingUserAccounts.add(userAccount(u2.getId(), Currency.USD));
        existingUserAccounts.add(userAccount(u2.getId(), Currency.GPB));

        userAccountRepository.saveAll(existingUserAccounts);
    }

    @After
    public void tearDown() {

    }

    protected static UserAccount userAccount(Long userId, Currency currency) {
        UserAccount account = new UserAccount();
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        account.setUserId(userId);
        return account;
    }
}
