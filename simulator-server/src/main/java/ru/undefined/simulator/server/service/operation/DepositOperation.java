package ru.undefined.simulator.server.service.operation;

import ru.undefined.simulator.commons.model.UserAccount;
import ru.undefined.simulator.server.repository.UserAccountRepository;

import java.math.BigDecimal;

public class DepositOperation extends Operation {
    @Override
    public BigDecimal perform(UserAccountRepository repository, Long userAccountId, BigDecimal amount) {
        logger.info("Deposit {} account id {}", amount, userAccountId);
        UserAccount account = getOrThrow(repository, userAccountId);
        BigDecimal updatedBalance = account.getBalance().add(amount);
        account.setBalance(updatedBalance);
        repository.save(account);
        after(updatedBalance, userAccountId);
        return updatedBalance;
    }
}
