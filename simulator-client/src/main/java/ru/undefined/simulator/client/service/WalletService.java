package ru.undefined.simulator.client.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.model.request.BalanceResponse;
import ru.undefined.simulator.model.request.SimpleResponse;
import ru.undefined.simulator.server.controller.WalletControllerGrpc;

import static ru.undefined.simulator.commons.Requests.balanceRequest;
import static ru.undefined.simulator.commons.Requests.transactionBody;

@Slf4j
@Service
public class WalletService {

    private final WalletControllerGrpc.WalletControllerBlockingStub stub;

    public WalletService(@Value("${simulator.server.host}") String host,
                         @Value("${simulator.server.port}") Integer port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = WalletControllerGrpc.newBlockingStub(channel);
    }

    public void deposit(Long userId, double amount, Currency currency) {
        SimpleResponse response = stub.deposit(transactionBody(userId, amount, currency.name()));
        log.info("Deposit response: {}", response);
    }

    public void withdraw(Long userId, double amount, Currency currency) {
        SimpleResponse response = stub.withdraw(transactionBody(userId, amount, currency.name()));
        log.info("Withdraw response: {}", response);
    }

    public void getBalance(Long userId) {
        BalanceResponse response = stub.getBalance(balanceRequest(userId));
        log.info("Balance response: {}", response);
    }

}
