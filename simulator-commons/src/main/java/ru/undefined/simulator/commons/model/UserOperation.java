package ru.undefined.simulator.commons.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "UserOperation")
public class UserOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Long userAccountId;

    private BigDecimal amount;

    private UserOperationType operationType;

    private OffsetDateTime date;

    private String error;

    public UserOperation(Long userAccountId, BigDecimal amount, UserOperationType operationType, OffsetDateTime date) {
        this.userAccountId = userAccountId;
        this.amount = amount;
        this.operationType = operationType;
        this.date = date;
    }
}
