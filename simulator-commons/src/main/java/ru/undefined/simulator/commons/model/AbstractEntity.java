package ru.undefined.simulator.commons.model;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    protected Long id;
}
