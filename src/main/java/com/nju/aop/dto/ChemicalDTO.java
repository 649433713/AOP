package com.nju.aop.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yinywf
 * Created on 2019-11-11
 */
@Data
public class ChemicalDTO implements Serializable {

    private Integer id;
    private String name;
    private String chinese;
    private String plastics;
    private String industrialRawMaterials;
    private String buildingMaterials;
    private String fireFighting;
    private String automobile;
    private String electronicProduct;
    private String insecticide;
    private String rodenticide;
    private String herbicide;
    private String sterilant;
    private String clean;
    private String veterinaryMedicine;
    private String petCare;
    private String humanMedicine;
    private String cigarette;
    private String childrenToy;
    private String foodAdditive;
    private List<String> cas;
    private List<Integer> aopId;
    private List<Integer> eventId;

}
