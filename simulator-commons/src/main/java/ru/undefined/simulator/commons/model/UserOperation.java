package ru.undefined.simulator.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "UserOperation")
@NoArgsConstructor
public class UserOperation {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private Long userAccountId;

    private BigDecimal amount;

    private BigDecimal updatedBalance;

    @Enumerated(EnumType.STRING)
    private UserOperationType operationType;

    private OffsetDateTime date;

    private String error;

    public UserOperation(Long userAccountId, BigDecimal amount, UserOperationType operationType) {
        this.userAccountId = userAccountId;
        this.amount = amount;
        this.operationType = operationType;
    }

    // TODO might be improved with separate boolean flag
    public boolean hasError() {
        return error != null;
    }
}
