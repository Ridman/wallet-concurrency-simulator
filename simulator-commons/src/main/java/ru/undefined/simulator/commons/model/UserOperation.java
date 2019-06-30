package ru.undefined.simulator.commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "UserOperation")
@Getter @Setter
public class UserOperation extends AbstractEntity {

    private Long userAccountId;

    private BigDecimal amount;

    private UserOperationType operationType;
}
