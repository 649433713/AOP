package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author yinywf
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aop {
    @Id
    @ExcelCell("ID")
    private Integer id;
    @ExcelCell("英文名")
    private String title;
    @ExcelCell("中文名")
    private String chinese;
    @ExcelCell("物种")
    private String species;
    @ExcelCell("性别")
    private String sex;
    @ExcelCell("生命周期")
    private String lifeCycle;
    @ExcelCell("器官/毒性类型")
    private String organ;
    @ExcelCell("致癌/遗传毒性")
    private String cancer;
    @ExcelCell("存活率")
    private String survivalRates;
    @ExcelCell("生物水平")
    private String level;

}
