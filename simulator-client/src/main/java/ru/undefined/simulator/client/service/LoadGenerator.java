package ru.undefined.simulator.client.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.User;
import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.server.repository.UserAccountRepository;
import ru.undefined.simulator.server.repository.UserOperationRepository;
import ru.undefined.simulator.server.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class LoadGenerator {

    @Autowired
    private RoundProvider roundProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserOperationRepository userOperationRepository;

    @Value("${simulator.user.count:0}")
    private Integer userCount;

    @Value("${simulator.user.threads:0}")
    private Integer requestsThreadCount;

    @Value("${simulator.round.threads:0}")
    private Integer roundsThreadCount;

    @Value("${simulator.user.rounds:0}")
    private Integer roundPerUserCount;

    public void generate() {
        // For testing purpose only
        userOperationRepository.deleteAll();
        userAccountRepository.deleteAll();
        userRepository.deleteAll();

        List<User> users = LongStream.range(0, userCount)
                .mapToObj(i -> new User()).collect(Collectors.toList());
        userRepository.saveAll(users);
        List<UserAccount> accounts = users.stream()
                .flatMap(user -> Stream.of(
                        new UserAccount(Currency.EUR, user.getId()),
                        new UserAccount(Currency.GPB, user.getId()),
                        new UserAccount(Currency.USD, user.getId())))
                .collect(Collectors.toList());

        userAccountRepository.saveAll(accounts);

        ExecutorService userTaskExecutor = Executors.newFixedThreadPool(userCount);
        ExecutorService shutdownInitiator = Executors.newSingleThreadExecutor();

        users.forEach(user -> {
            ExecutorService roundExecutor = Executors.newFixedThreadPool(roundsThreadCount);
            ExecutorService requestExecutor = Executors.newFixedThreadPool(requestsThreadCount);
            List<RoundAction> actions = IntStream.range(0, roundPerUserCount)
                    .mapToObj(i -> roundProvider.randomRound(user))
                    .map(requests -> new RoundAction(requests, requestExecutor))
                    .collect(Collectors.toList());
            UserTask task = new UserTask(actions, roundExecutor);
            userTaskExecutor.submit(task);
            shutdownInitiator.submit(() -> {
                try {
                    // TODO to be avoided somehow
                    Thread.sleep(2000);
                    roundExecutor.shutdown();
                    requestExecutor.shutdown();
                } catch (InterruptedException e) {
                    log.error("Sleep error", e);
                }
            });
        });
        shutdownInitiator.shutdown();
        userTaskExecutor.shutdown();
    }

    @AllArgsConstructor
    class UserTask implements Runnable {

        private List<RoundAction> actions;
        private ExecutorService roundExecutor;

        @Override
        public void run() {
            actions.forEach(roundExecutor::submit);
        }
    }

    @AllArgsConstructor
    class RoundAction implements Runnable {
        private Stream<Runnable> actions;
        private ExecutorService requestExecutor;

        @Override
        public void run() {
            actions.forEach(requestExecutor::submit);
        }
    }
}
