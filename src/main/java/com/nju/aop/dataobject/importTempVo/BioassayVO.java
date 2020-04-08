package com.nju.aop.dataobject.importTempVo;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinywf
 * Created on 2019-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BioassayVO {
    @ExcelCell("KE ID")
    private Integer eventId;
    @ExcelCell("检测类型/对象1")
    private String bioassay1;
    @ExcelCell("检测类型/对象2")
    private String bioassay2;
    @ExcelCell("检测效应")
    private String effect;

}
