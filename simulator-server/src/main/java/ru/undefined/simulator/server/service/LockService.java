package ru.undefined.simulator.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Slf4j
@Service
public class LockService {

    private final ConcurrentHashMap<String, Object> locks;

    private final ExecutorService transactionExecutor;

    private final BlockingQueue<String> keysQueue;

    private final Object innerMutex;

    public LockService(@Value("${simulator.server.pool.size:8}") Integer poolSize) {
        this.innerMutex = new Object();
        this.locks = new ConcurrentHashMap<>();
        this.transactionExecutor = Executors.newFixedThreadPool(poolSize);
        this.keysQueue = new ArrayBlockingQueue<>(poolSize);
    }

    public void runWithLock(Class lockedType, Object lockedId, Runnable operation) throws Exception {
        String key = createKey(lockedType, lockedId);
        Future future = null;
        synchronized (innerMutex) {
            Object opMutex = null;
            if (locks.contains(key)) {
                opMutex = locks.get(key);
            } else if (keysQueue.remainingCapacity() < 1) {
                String candidateToBeGone = keysQueue.take();
                locks.remove(candidateToBeGone);
                keysQueue.add(key);
            }
            future = transactionExecutor.submit(() -> {
                Object mutex = locks.computeIfAbsent(key, k -> new Object());
                synchronized (mutex) {
                    operation.run();
                }
            });
        }
        // Returns null if success, exception otherwise
        future.get();
    }

    private String createKey(Class lockedType, Object lockedId) {
        return lockedType.getSimpleName() + "_" + lockedId.toString();
    }

    private void emptyPlace() {

    }
}
