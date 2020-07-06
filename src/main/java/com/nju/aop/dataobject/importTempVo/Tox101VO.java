package com.nju.aop.dataobject.importTempVo;

import com.nju.aop.dataobject.Tox;
import com.nju.aop.utils.excel.ExcelCell;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * @author yinywf Created on 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tox101VO {

  @ExcelCell("化学品CAS号")
  private String casrn;
  @ExcelCell("化学品英文名")
  private String chemical;

  private String chemicalChinese;
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
  @ExcelCell("生物检测目标")
  private String intendedTargetFamily;

  private Double ac501;
  private Double ac502;
  private Double ac503;
  private Double ac504;
  private Double ac505;
  private Double ac506;
  private Double ac507;
  private Double ac508;
  private Double ac509;
  private Double ac510;
  private Double ac511;
  private Double ac512;
  private Double ac513;
  private Double ac514;
  private Double ac515;
  private Double ac516;
  private Double ac517;
  private Double ac518;
  private Double ac519;
  private Double ac520;
  private Double ac521;
  private Double ac522;
  private Double ac523;
  private Double ac524;
  private Double ac525;
  private Double ac526;
  private Double ac527;
  private Double ac528;
  private Double ac529;
  private Double ac530;
  private Double ac531;
  private Double ac532;
  private Double ac533;
  private Double ac534;
  private Double ac535;
  private Double ac536;
  private Double ac537;
  private Double ac538;
  private Double ac539;
  private Double ac540;
  private Double ac541;
  private Double ac542;
  private Double ac543;
  private Double ac544;
  private Double ac545;
  private Double ac546;
  private Double ac547;
  private Double ac548;
  private Double ac549;
  private Double ac550;
  private Double ac551;
  private Double ac552;
  private Double ac553;
  private Double ac554;
  private Double ac555;
  private Double ac556;
  private Double ac557;
  private Double ac558;
  private Double ac559;
  private Double ac560;
  private Double ac561;
  private Double ac562;
  private Double ac563;
  private Double ac564;
  private Double ac565;
  private Double ac566;
  private Double ac567;
  private Double ac568;
  private Double ac569;
  private Double ac570;
  private Double ac571;
  private Double ac572;
  private Double ac573;
  private Double ac574;
  private Double ac575;
  private Double ac576;
  private Double ac577;
  private Double ac578;
  private Double ac579;
  private Double ac580;
  private Double ac581;
  private Double ac582;
  private Double ac583;
  private Double ac584;
  private Double ac585;
  private Double ac586;
  private Double ac587;
  private Double ac588;
  private Double ac589;
  private Double ac590;
  private Double ac591;
  private Double ac592;
  private Double ac593;
  private Double ac594;
  private Double ac595;
  private Double ac596;
  private Double ac597;
  private Double ac598;
  private Double ac599;
  private Double ac600;
  private Double ac601;

  public Tox getTox() {
    Tox tox = new Tox();
    BeanUtils.copyProperties(this, tox);
    String bio = connect(connect(bioassay1, bioassay2), bioassay3);
    tox.setBioassay(bio);
    return tox;
  }

  public List<Pair<Integer, Double>> getAc(){
    List<Pair<Integer, Double>> result = new ArrayList<>();
    for (int i = 501; i < 602; i++) {
      String filedStr = "ac" + i;
      Field field = null;
      try {
        field = this.getClass().getDeclaredField(filedStr);
        field.setAccessible(true);
        Double d = (Double) field.get(this);
        if (d != null) {
          result.add(new Pair<>(i - 500, d));
        }
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return result;
  }


  private String connect(String s1, String s2) {
    if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
      return "";
    }
    if (StringUtils.isEmpty(s1)) {
      return s2;
    }
    if (StringUtils.isEmpty(s2)) {
      return s1;
    }
    return s1 + "," + s2;
  }
}
