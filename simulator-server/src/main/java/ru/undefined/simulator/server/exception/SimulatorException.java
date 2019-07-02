package ru.undefined.simulator.server.exception;

import ru.undefined.simulator.commons.model.Error;

public class SimulatorException extends RuntimeException {
    public SimulatorException(Error error) {
        super(error.getMessage());
    }
}
