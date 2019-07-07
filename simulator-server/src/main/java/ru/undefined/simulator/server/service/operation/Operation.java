package ru.undefined.simulator.server.service.operation;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.commons.model.UserOperationType;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserAccountRepository;

import java.math.BigDecimal;

@Slf4j
public abstract class Operation {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public abstract BigDecimal perform(UserAccountRepository repository, Long userAccountId, BigDecimal amount);

    UserAccount getOrThrow(UserAccountRepository repository, Long id) {
        return repository.findById(id).orElseThrow(() -> new SimulatorException(Error.NONE_APPROPRIATE_ACCOUNT_FOUND));
    }

    protected void after(BigDecimal updatedBalance, Long userAccountId) {
        logger.info("Account {} updated balance {}", userAccountId, updatedBalance);
    }

    public static Operation of(UserOperationType type) {
        switch (type) {
            case DEPOSIT:
                return new DepositOperation();
            case WITHDRAW:
                return new WithdrawOperation();
            default:
                throw new RuntimeException("Operation type not found");
        }
    }
}
