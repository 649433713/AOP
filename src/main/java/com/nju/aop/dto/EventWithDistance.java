package com.nju.aop.dto;

import com.nju.aop.dataobject.Event;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * created by Kimone
 * date 2020/1/7
 */
@Data
@AllArgsConstructor
public class EventWithDistance {
    private Event event;
    private Integer distance;
}
