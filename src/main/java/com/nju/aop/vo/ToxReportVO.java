package com.nju.aop.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Euterpe on 2020/6/22
 */
@Data
public class ToxReportVO {
    private String chemical;
    private List<TargetFamilyVO> targetFamilyVOList = new ArrayList<>();

    @Data
    public static class TargetFamilyVO{
        private String intendedTargetFamily;
        private int negative;
        private int positive;
        private Double lowestAC;
        private Double highestAC;
    }

}
