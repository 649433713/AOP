package com.nju.aop.controller;

import com.nju.aop.dataobject.*;
import com.nju.aop.dto.EventWithDistance;
import com.nju.aop.repository.*;
import com.nju.aop.dto.KEAndAO;
import com.nju.aop.service.ChemicalService;
import com.nju.aop.utils.ExampleMatcherUtil;
import com.nju.aop.vo.ChainVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.Even;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import scala.Int;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

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
    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private ChemicalService chemicalService;

    @PostMapping("/search/findByExample")
    public Page<Event> findByExample(@RequestBody Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

    @GetMapping("/search/findByExample2")
    public Page<Event> findByExample2(Event event, Pageable pageable) {

        return eventRepository.findAll(ExampleMatcherUtil.transfer(event), pageable);
    }

    @PostMapping("/search/findAOsByExample")
    public List<Event> findAOsByExample(@RequestBody Event event) {
        List<Event> events = eventRepository.findAll(ExampleMatcherUtil.transfer(event));
        List<Integer> aoIDList = chainRepository.findEventIdByType("AdverseOutcome");

        List<Event> aos = events.stream().filter(e -> aoIDList.contains(e.getId())).collect(Collectors.toList());
        return aos;
    }

    @GetMapping("/search/relativeAops")
    public List<Aop> findRelativeAops(@RequestParam Integer eventId) {
        List<Chain> chains = chainRepository.findByEventId(eventId);
        return aopRepository.findAllById(chains.stream().map(Chain::getAopId).collect(Collectors.toSet()));
    }

    @PostMapping("/findByBioassay")
    public List<KEAndAO> findByBioassay(@RequestParam String bioassay, @RequestParam String effect) {
        List<KEAndAO> list = chemicalService.findByBioassay(bioassay,effect);
        chemicalService.saveExcel(list, bioassay, effect);
        return list;
    }



    @GetMapping("/findAOsByQueryName")
    public List<Event> findAOByQueryName(@RequestParam String queryName) {
        Integer id = null;
        Pattern pattern = Pattern.compile("[0-9]*");
        if(pattern.matcher(queryName).matches()) {
            id = Integer.parseInt(queryName);
        }
        queryName = "%"+queryName+"%";
        List<Event> events = eventRepository.findByIdOrTitleLikeOrChineseLike(id, queryName, queryName);

        List<Integer> aoIDList = chainRepository.findEventIdByType("AdverseOutcome");

        List<Event> aos = events.stream().filter(event -> aoIDList.contains(event.getId())).collect(Collectors.toList());
        return aos;
    }


    @GetMapping("/findEventsByAO/{aoId}")
    public List<Event> findByAO(@PathVariable Integer aoId) {
        List<Event> events = new ArrayList<>();
        Set<Integer> eventIDs = new HashSet<>();
        List<Chain> chainWithAOId = chainRepository.findByEventIdAndType(aoId,"AdverseOutcome");
        for(Chain chain: chainWithAOId) {
            int aopId = chain.getAopId();
            List<Chain> chains = chainRepository.findByAopId(aopId);
            eventIDs.addAll(chains.stream().map(Chain::getEventId).collect(Collectors.toList()));
        }
        events.addAll(eventRepository.findAllById(eventIDs));
        return events;
    }

    @GetMapping("/findByAopId/{aopId}")
    public List<ChainVO> findByAOPId(@PathVariable Integer aopId) {
        List<Chain> chains = chainRepository.findByAopId(aopId);
        List<Integer> eventIds = chains.stream().map(Chain::getEventId).collect(Collectors.toList());
        Map<Integer, Event> eventMap = eventRepository.findAllById(eventIds)
                .stream().collect(Collectors.toMap(Event::getId,e -> e));

        List<ChainVO> ret = new ArrayList<>();
        chains.forEach(e -> {
            Event event = eventMap.get(e.getEventId());
            if(event != null){
                ChainVO chainVO = new ChainVO(event);
                chainVO.setType(e.getType());
                chainVO.setAopId(e.getAopId());
                ret.add(chainVO);
            }
        });

        return ret;
    }
}
