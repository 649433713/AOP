package com.nju.aop.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * created by Kimone
 * date 2020/1/12
 */
@Entity
@Data
public class Connectivity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private Integer componentId;
    private Integer eventId;

    public Connectivity(Integer componentId, Integer eventId) {
        this.componentId = componentId;
        this.eventId = eventId;
    }
}
