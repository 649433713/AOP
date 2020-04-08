package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author yinywf
 * Created on 2020-04-07
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chemical_brief", schema = "aop")
public class ChemicalBrief {
    @Id
    private int id;
    @ExcelCell("英文名")
    private String english;
    @ExcelCell("中文名")
    private String chinese;
    @ExcelCell("CAS号")
    private String cas;
    @ExcelCell("我国有无")
    private Byte beInChina;

}
