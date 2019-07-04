package ru.undefined.simulator.commons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Error {
    INSUFFICIENT_FUNDS,
    UNKNOWN_CURRENCY,
    USER_NOT_FOUND,
    NONE_APPROPRIATE_ACCOUNT_FOUND,
    INVALID_OPERATION,
    TRANSACTION_AMOUNT_SHOULD_BE_POSITIVE;
    
    public String getMessage() {
        return name().toLowerCase();
    }
}
