package com.nju.aop.dto;

import lombok.Data;

/**
 * created by Kimone
 * date 2020/1/12
 */
@Data
public class EventRank {
    private Integer eventId;
    private Double score;

    public EventRank(Integer eventId, Double score) {
        this.eventId = eventId;
        this.score = score;
    }
}
