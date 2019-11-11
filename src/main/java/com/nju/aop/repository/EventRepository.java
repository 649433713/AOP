package com.nju.aop.repository;


import com.nju.aop.dataobject.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yinywf
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

}
