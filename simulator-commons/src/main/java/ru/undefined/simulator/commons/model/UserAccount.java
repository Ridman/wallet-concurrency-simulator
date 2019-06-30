package ru.undefined.simulator.commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "User")
@Getter @Setter
public class UserAccount extends AbstractEntity {

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Long userId;
}
