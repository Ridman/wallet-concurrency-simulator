package ru.undefined.simulator.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.*;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserAccountRepository;
import ru.undefined.simulator.server.repository.UserOperationRepository;
import ru.undefined.simulator.server.service.operation.Operation;

import javax.persistence.OptimisticLockException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserOperationRepository userOperationRepository;

    @Autowired
    private OptimisticRetryableExecutor executor;

    public UserOperation deposit(Long userId, BigDecimal amount, Currency currency) {
        return performTransaction(userId, amount, currency, UserOperationType.DEPOSIT);
    }

    public UserOperation withdraw(Long userId, BigDecimal amount, Currency currency) {
        return performTransaction(userId, amount, currency, UserOperationType.WITHDRAW);
    }

    public List<UserAccount> getUserAccounts(Long userId) {
        userService.checkUserExists(userId);
        return userAccountRepository.findByUserId(userId);
    }

    private UserOperation performTransaction(Long userId, BigDecimal amount, Currency currency,
                                             UserOperationType operationType) {
        userService.checkUserExists(userId);
        // Operation balance should be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SimulatorException(Error.TRANSACTION_AMOUNT_SHOULD_BE_POSITIVE);
        }
        UserAccount userAccount = getUserAccount(userId, currency);
        final Long accountId = userAccount.getId();
        final UserOperation userOperation = new UserOperation(accountId, amount, operationType);
        final Operation operation = Operation.of(operationType);
        userOperation.setUpdatedBalance(userAccount.getBalance());
        Runnable action = () -> {
            try {
                userOperation.setDate(OffsetDateTime.now());
                BigDecimal updatedBalance = operation.perform(userAccountRepository, accountId, amount);
                userOperation.setUpdatedBalance(updatedBalance);
            } catch (SimulatorException ex) {
                log.error("Operation exception {}", ex.getMessage());
                userOperation.setError(ex.getMessage());
            } catch (Exception ex) {
                throw ex;
            }
        };
        executor.execute(action);
        return userOperationRepository.save(userOperation);
    }

    private UserAccount getUserAccount(Long userId, Currency currency) {
        UserAccount account = userAccountRepository.findByUserIdAndCurrency(userId, currency);
        if (Objects.isNull(account)) {
            throw new SimulatorException(Error.NONE_APPROPRIATE_ACCOUNT_FOUND);
        }
        return account;
    }

}
