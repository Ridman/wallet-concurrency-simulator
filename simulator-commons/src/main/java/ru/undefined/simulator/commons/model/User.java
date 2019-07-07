package ru.undefined.simulator.commons.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "User")
@NoArgsConstructor
public class User extends AbstractEntity {

    //    @OneToMany(fetch = FetchType.LAZY)
//    private List<UserAccount> userBalances;
}
