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
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event", schema = "aop")
public class MieInteractionType {
    @Id
    @ExcelCell("MIE ID")
    private Integer id;
    @ExcelCell("英文名")
    private String title;
    @ExcelCell("中文名")
    private String chinese;
    @ExcelCell("类型")
    private String mieType;

}
