package com.nju.aop.vo;

import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Euterpe on 2020/6/20
 */
@Data
@AllArgsConstructor
public class ChainVO {
    private Integer aopId;
    private Integer eventId;
    private String title;
    private String chinese;
    private String species;
    private String organ;
    private String cancer;
    private String lifeCycle;
    private String level;
    private String type;
    private String sex;
    public ChainVO(Event e) {
        this.eventId = e.getId();
        this.cancer = e.getCancer();
        this.title = e.getTitle();
        this.chinese = e.getChinese();
        this.species = e.getSpecies();
        this.organ = e.getOrgan();
        this.lifeCycle = e.getLifeCycle();
        this.level = e.getLevel();
        this.sex = e.getSex();
    }
}
