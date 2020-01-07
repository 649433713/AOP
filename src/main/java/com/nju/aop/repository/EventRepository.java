package com.nju.aop.repository;


import com.nju.aop.dataobject.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByIdOrTitleLikeOrChineseLike(Integer id, String title, String chinese);
}
