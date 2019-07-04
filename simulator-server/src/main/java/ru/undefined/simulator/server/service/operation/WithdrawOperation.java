package ru.undefined.simulator.server.service.operation;

import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserAccountRepository;

import java.math.BigDecimal;

public class WithdrawOperation extends Operation {
    @Override
    public BigDecimal perform(UserAccountRepository repository, Long userAccountId, BigDecimal amount) {
        logger.info("Withdraw {} account id {}", amount, userAccountId);
        UserAccount account = getOrThrow(repository, userAccountId);
        BigDecimal actualBalance = account.getBalance();
        if (actualBalance.compareTo(amount) < 0) {
            throw new SimulatorException(Error.INSUFFICIENT_FUNDS);
        }
        BigDecimal updatedBalance = actualBalance.subtract(amount);
        account.setBalance(updatedBalance);
        repository.save(account);
        after(updatedBalance, userAccountId);
        return updatedBalance;
    }
}
