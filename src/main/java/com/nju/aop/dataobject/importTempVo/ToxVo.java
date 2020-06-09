package com.nju.aop.dataobject.importTempVo;

import com.nju.aop.dataobject.Tox;
import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author yinywf
 * Created on 2019-11-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToxVo {
    //assay_name, bioassay, casrn, chemical, effect, intended_target_family
    @ExcelCell("生物检测方法ID")
    private String toxId;
    @ExcelCell("化学品CAS号")
    private String casrn;
    @ExcelCell("化学品英文名")
    private String chemical;
    @ExcelCell("生物检测名称")
    private String assayName;
    @ExcelCell("生物检测目标1")
    private String bioassay1;
    @ExcelCell("生物检测目标2")
    private String bioassay2;
    @ExcelCell("生物检测目标3")
    private String bioassay3;
    @ExcelCell("生物检测效应")
    private String effect;
    @ExcelCell("检测目标类型")
    private String intendedTargetFamily;
    @ExcelCell("AC50")
    private Double ac50;

    public Tox getTox() {
        Tox tox = new Tox();
        BeanUtils.copyProperties(this, tox);
        String bio = connect(connect(bioassay1, bioassay2), bioassay3);
        tox.setBioassay(bio);
        return tox;
    }

    private String connect(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return "";
        }
        if (s1 == null) {
            return s2;
        }
        if (s2 == null) {
            return s1;
        }
        return s1 + "," + s2;
    }

}
