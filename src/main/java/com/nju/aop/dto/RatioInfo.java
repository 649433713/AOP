package com.nju.aop.dto;

import lombok.Data;

/**
 * created by Kimone
 * date 2020/2/3
 */
@Data
public class RatioInfo {
    private Integer aoId;
    private Double pageRank;
    private Double species;
    private Double sex;
    private Double lifeCycle;
    private Double organ;
    private Double cancer;
    private Double survivalRates;
    private Double level;
}
