package ru.undefined.simulator.server.controller;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.undefined.simulator.commons.function.TriFunction;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.commons.model.UserOperation;
import ru.undefined.simulator.model.request.*;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GRpcService
public class WalletController extends WalletControllerGrpc.WalletControllerImplBase {

    @Autowired
    private AccountService accountService;

    @Override
    public void withdraw(TransactionBody body, StreamObserver<SimpleResponse> responseObserver) {
        SimpleResponse response = executeTransaction(body, accountService::withdraw);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deposit(TransactionBody body, StreamObserver<SimpleResponse> responseObserver) {
        SimpleResponse response = executeTransaction(body, accountService::deposit);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBalance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        List<UserAccount> accounts = accountService.getUserAccounts(request.getUserId());
        BalanceResponse response = BalanceResponse.newBuilder()
                .putAllBalances(accounts.stream().collect(Collectors
                        .toMap(acc -> acc.getCurrency().name(), acc -> acc.getBalance().doubleValue())))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private SimpleResponse executeTransaction(TransactionBody body,
                                              TriFunction<Long, BigDecimal, Currency, UserOperation> executor) {
        SimpleResponse.Builder responseBuilder = SimpleResponse.newBuilder();
        UserOperation operation = null;
        try {
            Long userId = body.getUserId();
            BigDecimal amount = BigDecimal.valueOf(body.getAmount());

            operation = executor.apply(userId, amount, resolveCurrency(body.getCurrency()));
            if (operation.hasError()) {
                responseBuilder.setMessage(operation.getError());
            }
        } catch (Exception ex) {
            responseBuilder.setMessage(ex.getMessage());
        } finally {
            boolean success = operation != null && !operation.hasError();
            responseBuilder.setStatus(success ? ResponseStatus.OK : ResponseStatus.ERROR);
        }
        return responseBuilder.build();
    }

    private Currency resolveCurrency(String currency) {
        try {
            return Currency.valueOf(currency);
        } catch (Exception ex) {
            log.error("Currency convert exception", ex);
            throw new SimulatorException(Error.UNKNOWN_CURRENCY);
        }
    }
}
