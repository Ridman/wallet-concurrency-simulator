package ru.undefined.simulator.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.*;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserAccountRepository;
import ru.undefined.simulator.server.repository.UserOperationRepository;
import ru.undefined.simulator.server.repository.UserRepository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Slf4j
@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserOperationRepository userOperationRepository;

    @Autowired
    private LockService lockService;

    public UserOperation deposit(Long userId, BigDecimal amount, Currency currency) {
        final UserAccount account = getUserAccount(userId, currency);
        Long accountId = account.getId();
        OffsetDateTime now = OffsetDateTime.now();
        UserOperation userOperation = new UserOperation(accountId, amount, UserOperationType.DEPOSIT, now);
        preLog(userOperation);
        lockService.runWithLock(UserAccount.class, accountId, () -> {
            BigDecimal balance = account.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                userOperation.setError(Error.INVALID_OPERATION.getMessage());
            } else if (balance.compareTo(amount) < 0) {
                userOperation.setError(Error.INSUFFICIENT_FUNDS.getMessage());
            } else {
                BigDecimal newBalance = balance.subtract(amount);
                account.setAmount(newBalance);
                userAccountRepository.save(account);
            }
        });
        postLog(userOperation);
        return userOperationRepository.save(userOperation);
    }

    public UserOperation withdraw(Long userId, BigDecimal amount, Currency currency) {
        return null;
    }

    public BigDecimal getBalance(Long userId) {
        return null;
    }

    private UserAccount getUserAccount(Long userId, Currency currency) {
        if (userRepository.existsById(userId)) {
            throw new SimulatorException(Error.USER_NOT_FOUND);
        }
        UserAccount account = userAccountRepository.findByUserIdAndCurrency(userId, currency);
        if (Objects.isNull(account)) {
            throw new SimulatorException(Error.NONE_APPROPRIATE_ACCOUNT_FOUND);
        }
        return account;
    }

    private void preLog(UserOperation operation) {
        log.trace("Handling {}", operation);
    }

    private void postLog(UserOperation operation) {
        if (operation.getError() != null) {
            log.trace("Rejected {}", operation);
        } else {
            log.trace("Succeeded {}", operation);
        }
    }

}
