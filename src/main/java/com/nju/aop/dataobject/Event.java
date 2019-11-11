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
public class Event {
    @Id
    private Integer id;
    private String title;
    @ExcelCell("汉化")
    private String chinese;
    @ExcelCell("物种")
    private String species;
    @ExcelCell("性别")
    private String sex;
    @ExcelCell("生命周期")
    private String lifeCycle;
    @ExcelCell("器官")
    private String organ;
    @ExcelCell("癌症")
    private String cancer;
    @ExcelCell("存活率")
    private String survivalRates;
    @ExcelCell("水平")
    private String level;

}
