package com.nju.aop.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author yinywf
 * Created on 2019-11-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bioassay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer eventId;
    private String bioassayName;
    private String effect;

}
