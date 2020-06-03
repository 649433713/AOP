package com.nju.aop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinywf
 * Created on 2020-06-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDTO {

    private Integer id;
    private Integer sourceId;
    private String sourceTitle;
    private String sourceChinese;
    private Integer targetId;
    private String targetTitle;
    private String targetChinese;

}
