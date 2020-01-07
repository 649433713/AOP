package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author yinywf
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ExcelCell("AOP ID")
    private Integer aopId;
    @ExcelCell("EventID")
    private Integer eventId;
    @ExcelCell("MIE/KE/AO")
    private String type;

    private String name;

}
