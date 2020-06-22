package com.nju.aop.dataobject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinywf Created on 2020/6/22
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tox_count", schema = "aop")
public class ToxCount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String alive;
  private String intendedTargetFamily;
  private String casrn;
  private String chemical;
  private String chemicalChinese;
  private Integer count;
}
