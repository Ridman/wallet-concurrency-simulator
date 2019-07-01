package ru.undefined.simulator.commons.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "UserOperation")
public class UserOperation {

    private UUID id;

    private Long userAccountId;

    private BigDecimal amount;

    private UserOperationType operationType;

    private OffsetDateTime date;
}
