package ru.undefined.simulator.commons.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "UserAccount")
@Getter @Setter
public class UserAccount extends AbstractEntity {

    public UserAccount() {
        this.balance = BigDecimal.ZERO;
    }

    public UserAccount(Currency currency, Long userId) {
        this();
        this.currency = currency;
        this.userId = userId;
    }

    @Column(name = "amount")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Long userId;

    @Version
    private Integer version;
}
