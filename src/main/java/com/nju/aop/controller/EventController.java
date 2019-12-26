package com.nju.aop.controller;

import com.nju.aop.dataobject.Aop;
import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Event;
import com.nju.aop.repository.AopRepository;
import com.nju.aop.repository.BioassayRepository;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.EventRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ChainRepository chainRepository;
    @Autowired
    private AopRepository aopRepository;
    @Autowired
    private BioassayRepository bioassayRepository;


    @PostMapping("/search/findByExample")
    public Page<Event> findByExample(@RequestBody Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

    @GetMapping("/search/findByExample2")
    public Page<Event> findByExample2(Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

    @GetMapping("/search/relativeAops")
    public List<Aop> findRelativeAops(@RequestParam Integer eventId) {
        List<Chain> chains = chainRepository.findByEventId(eventId);
        return aopRepository.findAllById(chains.stream().map(Chain::getAopId).collect(Collectors.toSet()));
    }

}
