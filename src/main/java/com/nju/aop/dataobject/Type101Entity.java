package com.nju.aop.dataobject;

import javax.persistence.Entity;
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
@Table(name = "type101", schema = "aop")
public class Type101Entity {

  @Id
  private int id;
  private String english;
  private String chinese;
  private String cas;
  private String code;
}
