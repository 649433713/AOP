package com.nju.aop.dataobject;

import com.nju.aop.utils.excel.ExcelCell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author yinywf
 * Created on 2019-11-06
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chemical implements Serializable {
    @Id
    @ExcelCell("序号")
    private Integer id;
    @ExcelCell("名称")
    private String name;
    @ExcelCell("中文")
    private String chinese;
    @ExcelCell("塑料")
    private String plastics;
    @ExcelCell("工业原料")
    private String industrialRawMaterials;
    @ExcelCell("建筑材料")
    private String buildingMaterials;
    @ExcelCell("防火灭火")
    private String fireFighting;
    @ExcelCell("汽车、船等")
    private String automobile;
    @ExcelCell("电子产品")
    private String electronicProduct;
    @ExcelCell("杀虫剂")
    private String insecticide;
    @ExcelCell("灭鼠剂")
    private String rodenticide;
    @ExcelCell("除草剂")
    private String herbicide;
    @ExcelCell("灭菌剂")
    private String sterilant;
    @ExcelCell("清洗")
    private String clean;
    @ExcelCell("兽药")
    private String veterinaryMedicine;
    @ExcelCell("宠物护理")
    private String petCare;
    @ExcelCell("人类医药")
    private String humanMedicine;
    @ExcelCell("香烟")
    private String cigarette;
    @ExcelCell("孩童玩具等")
    private String childrenToy;
    @ExcelCell("食品添加剂")
    private String foodAdditive;

}
