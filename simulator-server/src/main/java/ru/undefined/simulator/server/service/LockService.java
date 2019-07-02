package ru.undefined.simulator.server.service;

import org.springframework.stereotype.Service;

@Service
public class LockService {
    public void runWithLock(Class lockedType, Object lockedId, Runnable operation) {

    }
}
