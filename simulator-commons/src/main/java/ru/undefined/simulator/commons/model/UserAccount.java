package ru.undefined.simulator.commons.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "UserAccount")
@Getter @Setter
public class UserAccount extends AbstractEntity {

    @Column(name = "amount")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Long userId;

    @Version
    private Integer version;
}
