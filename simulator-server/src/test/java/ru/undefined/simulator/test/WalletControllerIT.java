package ru.undefined.simulator.test;

import io.grpc.stub.StreamObserver;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.User;
import ru.undefined.simulator.model.request.BalanceRequest;
import ru.undefined.simulator.model.request.ResponseStatus;
import ru.undefined.simulator.model.request.SimpleResponse;
import ru.undefined.simulator.model.request.TransactionBody;
import ru.undefined.simulator.server.controller.WalletController;
import ru.undefined.simulator.test.helpers.observer.BalanceValidationStreamObserver;
import ru.undefined.simulator.test.helpers.observer.ErrorValidationStreamObserver;

import java.math.BigDecimal;

public class WalletControllerIT extends AbstractIT {

    @Autowired
    private WalletController walletController;

    @Test
    public void whenInsufficientFundsThenAppropriateMessage() {
        User user = existingUsers.get(0);

        // 1. Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
        walletController.withdraw(transactionBody(user, BigDecimal.valueOf(200), Currency.USD),
                insufficientFundsValidator());
        // 2. Make a deposit of USD 100 to user with id 1.
        walletController.deposit(transactionBody(user, BigDecimal.valueOf(100), Currency.USD), okValidator());
        // 3. Check that all balances are correct.
        BalanceValidationStreamObserver balanceObserver = new BalanceValidationStreamObserver();
        walletController.getBalance(balanceRequest(user), balanceObserver);
        balanceObserver.assertBalance(Currency.USD, 100);
        balanceObserver.assertBalance(Currency.EUR, 0);
        balanceObserver.assertBalance(Currency.GPB, 0);
        // 4. Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
        walletController.withdraw(transactionBody(user, BigDecimal.valueOf(200), Currency.USD),
                insufficientFundsValidator());
        // 5. Make a deposit of EUR 100 to user with id 1.
        walletController.deposit(transactionBody(user, BigDecimal.valueOf(100), Currency.EUR), okValidator());
        // 6. Check that all balances are correct.
        walletController.getBalance(balanceRequest(user), balanceObserver);
        balanceObserver.assertBalance(Currency.USD, 100);
        balanceObserver.assertBalance(Currency.EUR, 100);
        balanceObserver.assertBalance(Currency.GPB, 0);
        // 7. Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
        walletController.withdraw(transactionBody(user, BigDecimal.valueOf(200), Currency.USD),
                insufficientFundsValidator());
        // 8. Make a deposit of USD 100 to user with id 1.
        walletController.deposit(transactionBody(user, BigDecimal.valueOf(100), Currency.USD), okValidator());
        // 9. Check that all balances are correct.
        walletController.getBalance(balanceRequest(user), balanceObserver);
        balanceObserver.assertBalance(Currency.USD, 200);
        balanceObserver.assertBalance(Currency.EUR, 100);
        balanceObserver.assertBalance(Currency.GPB, 0);
        // 10. Make a withdrawal of USD 200 for user with id 1. Must return "ok".
        walletController.withdraw(transactionBody(user, BigDecimal.valueOf(200), Currency.USD), okValidator());
        // 11. Check that all balances are correct.
        walletController.getBalance(balanceRequest(user), balanceObserver);
        balanceObserver.assertBalance(Currency.USD, 0);
        balanceObserver.assertBalance(Currency.EUR, 100);
        balanceObserver.assertBalance(Currency.GPB, 0);
        // 12. Make a withdrawal of USD 200 for user with id 1. Must return "insufficient_funds".
        walletController.withdraw(transactionBody(user, BigDecimal.valueOf(200), Currency.USD),
                insufficientFundsValidator());
    }

    private TransactionBody transactionBody(User user, BigDecimal amount, Currency currency) {
        return TransactionBody.newBuilder()
                .setUserId(user.getId())
                .setAmount(amount.doubleValue())
                .setCurrency(currency.name())
                .build();
    }

    private BalanceRequest balanceRequest(User user) {
        return BalanceRequest.newBuilder()
                .setUserId(user.getId())
                .build();
    }

    private StreamObserver<SimpleResponse> insufficientFundsValidator() {
        return new ErrorValidationStreamObserver(Error.INSUFFICIENT_FUNDS.getMessage(), ResponseStatus.ERROR);
    }

    private StreamObserver<SimpleResponse> okValidator() {
        return new ErrorValidationStreamObserver("", ResponseStatus.OK);
    }
}
