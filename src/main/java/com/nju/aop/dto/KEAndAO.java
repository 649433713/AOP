package com.nju.aop.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nju.aop.dataobject.Event;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * created by Kimone
 * date 2019/12/26
 */
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class KEAndAO implements Serializable {
    private Event KE;
    private List<EventWithDistance> AOs;
}
