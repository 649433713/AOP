package com.nju.aop.dataobject.importTempVo;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.Data;

/**
 * @author yinywf
 * Created on 2019-11-07
 */
@Data
public class CasEtc {

    @ExcelCell("序号")
    private Integer chemicalId;
    private String cas1;
    private String cas2;
    private String cas3;
    private Integer AOPID1;
    private Integer AOPID2;
    private Integer AOPID3;
    private Integer AOPID4;
    private Integer AOPID5;
    private Integer IDKE1;
    private Integer IDKE2;
    private Integer IDKE3;
    private Integer IDKE4;
    private Integer IDKE5;
    private Integer IDKE6;
    private Integer IDKE7;
    private Integer IDKE8;
    private Integer IDKE9;
    private Integer IDKE10;
    private Integer IDKE11;
}
