package ru.undefined.simulator.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.undefined.simulator.commons.model.Error;
import ru.undefined.simulator.server.exception.SimulatorException;
import ru.undefined.simulator.server.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void checkUserExists(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new SimulatorException(Error.USER_NOT_FOUND);
        }
    }
}
