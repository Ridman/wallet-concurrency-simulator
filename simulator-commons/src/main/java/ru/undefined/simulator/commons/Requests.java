package ru.undefined.simulator.commons;

import ru.undefined.simulator.model.request.BalanceRequest;
import ru.undefined.simulator.model.request.TransactionBody;

public class Requests {
    public static TransactionBody transactionBody(Long userId, double amount, String currency) {
        return TransactionBody.newBuilder()
                .setUserId(userId)
                .setAmount(amount)
                .setCurrency(currency)
                .build();
    }

    public static BalanceRequest balanceRequest(Long userId) {
        return BalanceRequest.newBuilder()
                .setUserId(userId)
                .build();
    }
}
