package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author yinywf
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Biodetection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ExcelCell("MIE ID")
    private Integer mieId;
    @ExcelCell("检测终点")
    private String endPoint;
    @ExcelCell("检测类型")
    private String type;
    @ExcelCell("检测名称")
    private String name;
    @ExcelCell("检测载体")
    private String carrier;
    @ExcelCell("检测成分")
    private String component;
    @ExcelCell("阳性对照")
    private String activeControl;
    @ExcelCell("时间（hr）")
    private String time;
    @ExcelCell("适用物种")
    private String applicableSpecies;
    @ExcelCell("信号说明")
    @Column(name = "_signal")
    private String signal;
    @ExcelCell("来源")
    private String source;

}
