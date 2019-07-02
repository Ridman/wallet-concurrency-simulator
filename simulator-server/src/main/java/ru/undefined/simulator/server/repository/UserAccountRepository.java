package ru.undefined.simulator.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.undefined.simulator.commons.model.Currency;
import ru.undefined.simulator.commons.model.UserAccount;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByUserIdAndCurrency(Long userId, Currency currency);
}
