package ru.undefined.simulator.commons.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "User")
public class User extends AbstractEntity {

    @OneToMany(fetch = FetchType.LAZY)
    private List<UserAccount> userBalances;
}
