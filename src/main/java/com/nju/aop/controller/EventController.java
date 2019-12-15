package com.nju.aop.controller;

import com.nju.aop.dataobject.Event;
import com.nju.aop.repository.EventRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author yinywf
 * Created on 2019-12-11
 */
@RestController
@Slf4j
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/search/findByExample")
    public Page<Event> findByExample(@RequestBody Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

    @GetMapping("/search/findByExample2")
    public Page<Event> findByExample2(Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

}
