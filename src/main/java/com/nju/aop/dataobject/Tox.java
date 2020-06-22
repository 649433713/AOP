package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import javax.persistence.Column;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author yinywf
 * Created on 2019-11-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tox", schema = "aop")
public class Tox {
    //assay_name, bioassay, casrn, chemical, effect, intended_target_family
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String toxId;
    private String casrn;
    private String chemical;
    @Column(name = "chemical_chinese")
    private String chemicalChinese;
    @Column(name = "chemical_code")
    private String chemicalCode;
    @ExcelCell("Assay.name")
    private String assayName;
    private String bioassay;
    private String effect;
    @ExcelCell("intended_target_family")
    private String intendedTargetFamily;
    private Double ac50;


}
