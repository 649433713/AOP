package com.nju.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by Kimone
 * date 2020/6/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxDTO {
    private Integer id;
    private String toxId;
    private String casrn;
    private String chemical;
    private String assayName;
    private String bioassay;
    private String effect;
    private String intendedTargetFamily;
    private Double ac50;
    private boolean hasRes;
}
