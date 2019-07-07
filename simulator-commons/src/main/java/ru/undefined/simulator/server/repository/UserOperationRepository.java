package ru.undefined.simulator.server.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.undefined.simulator.commons.model.UserOperation;

import java.util.UUID;

@Repository
public interface UserOperationRepository extends CrudRepository<UserOperation, UUID> {
}
