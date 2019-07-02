package ru.undefined.simulator.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.undefined.simulator.commons.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
