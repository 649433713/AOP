package com.nju.aop.dataobject.importTempVo;

import com.nju.aop.utils.excel.ExcelCell;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinywf Created on 2020/6/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tox101CountVO {

  @ExcelCell("有无生物活性")
  private String alive;
  @ExcelCell("生物检测类型")
  private String intendedTargetFamily;

  private Integer ac501;
  private Integer ac502;
  private Integer ac503;
  private Integer ac504;
  private Integer ac505;
  private Integer ac506;
  private Integer ac507;
  private Integer ac508;
  private Integer ac509;
  private Integer ac510;
  private Integer ac511;
  private Integer ac512;
  private Integer ac513;
  private Integer ac514;
  private Integer ac515;
  private Integer ac516;
  private Integer ac517;
  private Integer ac518;
  private Integer ac519;
  private Integer ac520;
  private Integer ac521;
  private Integer ac522;
  private Integer ac523;
  private Integer ac524;
  private Integer ac525;
  private Integer ac526;
  private Integer ac527;
  private Integer ac528;
  private Integer ac529;
  private Integer ac530;
  private Integer ac531;
  private Integer ac532;
  private Integer ac533;
  private Integer ac534;
  private Integer ac535;
  private Integer ac536;
  private Integer ac537;
  private Integer ac538;
  private Integer ac539;
  private Integer ac540;
  private Integer ac541;
  private Integer ac542;
  private Integer ac543;
  private Integer ac544;
  private Integer ac545;
  private Integer ac546;
  private Integer ac547;
  private Integer ac548;
  private Integer ac549;
  private Integer ac550;
  private Integer ac551;
  private Integer ac552;
  private Integer ac553;
  private Integer ac554;
  private Integer ac555;
  private Integer ac556;
  private Integer ac557;
  private Integer ac558;
  private Integer ac559;
  private Integer ac560;
  private Integer ac561;
  private Integer ac562;
  private Integer ac563;
  private Integer ac564;
  private Integer ac565;
  private Integer ac566;
  private Integer ac567;
  private Integer ac568;
  private Integer ac569;
  private Integer ac570;
  private Integer ac571;
  private Integer ac572;
  private Integer ac573;
  private Integer ac574;
  private Integer ac575;
  private Integer ac576;
  private Integer ac577;
  private Integer ac578;
  private Integer ac579;
  private Integer ac580;
  private Integer ac581;
  private Integer ac582;
  private Integer ac583;
  private Integer ac584;
  private Integer ac585;
  private Integer ac586;
  private Integer ac587;
  private Integer ac588;
  private Integer ac589;
  private Integer ac590;
  private Integer ac591;
  private Integer ac592;
  private Integer ac593;
  private Integer ac594;
  private Integer ac595;
  private Integer ac596;
  private Integer ac597;
  private Integer ac598;
  private Integer ac599;
  private Integer ac600;
  private Integer ac601;


  public List<Pair<Integer, Integer>> getAc(){
    List<Pair<Integer, Integer>> result = new ArrayList<>();
    for (int i = 501; i < 602; i++) {
      String filedStr = "ac" + i;
      Field field = null;
      try {
        field = this.getClass().getDeclaredField(filedStr);
        field.setAccessible(true);
        Integer d = (Integer) field.get(this);
        if (d != null) {
          result.add(new Pair<>(i - 500, d));
        }
      } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return result;
  }


}
