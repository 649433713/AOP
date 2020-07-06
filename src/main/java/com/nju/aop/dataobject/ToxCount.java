package com.nju.aop.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Euterpe on 2020/6/22
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String alive;
    private String intendedTargetFamily;
    private String casrn;
    private String chemical;
    private String chemicalChinese;
    private int count;
}
