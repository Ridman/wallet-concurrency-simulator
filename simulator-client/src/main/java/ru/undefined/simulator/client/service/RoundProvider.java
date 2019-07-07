package ru.undefined.simulator.client.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.client.service.WalletService;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.User;

import java.util.Random;
import java.util.stream.Stream;

import static ru.undefined.simulator.commons.model.Currency.*;

@Service
public class RoundProvider {

    @Autowired
    private WalletService service;

    private Random random = new Random();

    public Stream<Runnable> randomRound(User user) {
        if (random.nextBoolean()) {
            return roundA(user);
        } else if (random.nextBoolean()) {
            return roundB(user);
        } else {
            return roundC(user);
        }
    }

    public Stream<Runnable> roundA(User user) {
        return Stream.of(
                new Deposit(user, 100, USD),
                new Withdraw(user, 200, USD),
                new Deposit(user, 100, EUR),
                new GetBalance(user),
                new Withdraw(user, 100, USD),
                new GetBalance(user),
                new Withdraw(user, 100, USD));
    }

    public Stream<Runnable> roundB(User user) {
        return Stream.of(
                new Withdraw(user, 100, GPB),
                new Deposit(user, 300, GPB),
                new Withdraw(user, 100, GPB),
                new Withdraw(user, 100, GPB),
                new Withdraw(user, 100, GPB));
    }

    public Stream<Runnable> roundC(User user) {
        return Stream.of(
                new GetBalance(user),
                new Deposit(user, 100, USD),
                new Deposit(user, 100, USD),
                new Withdraw(user, 100, USD),
                new Deposit(user, 100, USD),
                new GetBalance(user),
                new Withdraw(user, 200, USD),
                new GetBalance(user));
    }

    @AllArgsConstructor
    class Deposit implements Runnable {

        private User user;
        private double amount;
        private Currency currency;

        @Override
        public void run() {
            service.deposit(user.getId(), amount, currency);
        }
    }

    @AllArgsConstructor
    class Withdraw implements Runnable {

        private User user;
        private double amount;
        private Currency currency;

        @Override
        public void run() {
            service.withdraw(user.getId(), amount, currency);
        }
    }

    @AllArgsConstructor
    class GetBalance implements Runnable {

        private User user;

        @Override
        public void run() {
            service.getBalance(user.getId());
        }
    }
}
