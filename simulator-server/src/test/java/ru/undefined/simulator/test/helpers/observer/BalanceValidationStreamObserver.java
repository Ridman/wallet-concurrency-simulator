package ru.undefined.simulator.test.helpers.observer;

import io.grpc.stub.StreamObserver;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.model.request.BalanceResponse;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class BalanceValidationStreamObserver implements StreamObserver<BalanceResponse> {

    private Map<Currency, BigDecimal> actualBalances;

    @Override
    public void onNext(BalanceResponse value) {
        actualBalances = value.getBalancesMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Currency.valueOf(entry.getKey()),
                        entry -> BigDecimal.valueOf(entry.getValue())
                ));
    }

    @Override
    public void onError(Throwable t) {
        throw new RuntimeException(t);
    }

    @Override
    public void onCompleted() {
    }

    public void assertBalance(Currency currency, double value) {
        BigDecimal expected = BigDecimal.valueOf(value);
        BigDecimal actual = actualBalances.get(currency);
        if (!actualBalances.containsKey(currency) || !expected.equals(actual)) {
            throw new AssertionError(String.format("Currency %s - Expected: %s\tActual: %s",
                    currency, expected, actual));
        }
    }
}