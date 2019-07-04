package ru.undefined.simulator.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.*;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserAccountRepository;
import ru.undefined.simulator.server.repository.UserOperationRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private LockService lockService;

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
        // Operation amount should be positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SimulatorException(Error.TRANSACTION_AMOUNT_SHOULD_BE_POSITIVE);
        }
        final UserAccount account = getUserAccount(userId, currency);
        final Long accountId = account.getId();
        final OffsetDateTime now = OffsetDateTime.now();
        final UserOperation userOperation = new UserOperation(accountId, amount, UserOperationType.DEPOSIT, now);
        // Thread-safe transaction success mark
        final AtomicBoolean success = new AtomicBoolean(false);
        preLog(userOperation);
        try {
            // Get and update balance atomically for account
            lockService.runWithLock(UserAccount.class, accountId, () -> {
                BigDecimal balance = userAccountRepository.getBalanceById(accountId);
                boolean isDeposit = operationType == UserOperationType.DEPOSIT;
                if (balance.compareTo(amount) < 0 && !isDeposit) {
                    userOperation.setError(Error.INSUFFICIENT_FUNDS.getMessage());
                } else {
                    BigDecimal newBalance = isDeposit ? balance.add(amount) : balance.subtract(amount);
                    account.setAmount(newBalance);
                    userAccountRepository.save(account);
                    success.set(true);
                }
            });
            postLog(userOperation, success.get());
        } catch (Exception ex) {
            log.error("Cannot perform {}", userOperation, ex);
        }
        return userOperationRepository.save(userOperation);
    }

    private UserAccount getUserAccount(Long userId, Currency currency) {
        UserAccount account = userAccountRepository.findByUserIdAndCurrency(userId, currency);
        if (Objects.isNull(account)) {
            throw new SimulatorException(Error.NONE_APPROPRIATE_ACCOUNT_FOUND);
        }
        return account;
    }

    private void preLog(UserOperation operation) {
        log.trace("Handling {}", operation);
    }

    private void postLog(UserOperation operation, boolean success) {
        if (success) {
            log.trace("Succeeded {}", operation);
        } else {
            log.trace("Rejected {}", operation);
        }
    }

}
