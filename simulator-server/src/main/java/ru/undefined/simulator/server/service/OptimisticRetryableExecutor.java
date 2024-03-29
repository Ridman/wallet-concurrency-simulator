package ru.undefined.simulator.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;

@Slf4j
@Service
public class OptimisticRetryableExecutor {

    public void execute(Runnable runnable) {
        boolean success =false;
        while (!success) {
            try {
                runnable.run();
                success = true;
            } catch (OptimisticLockException|ObjectOptimisticLockingFailureException ex) {
                log.warn("Optimistic lock occurred, retrying");
            } catch (Exception ex) {
                log.error("Something went wrong", ex);
                break;
            }
        }
    }
}
