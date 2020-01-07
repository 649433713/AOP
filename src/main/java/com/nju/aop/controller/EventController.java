package com.nju.aop.controller;

import com.nju.aop.dataobject.Aop;
import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Event;
import com.nju.aop.repository.AopRepository;
import com.nju.aop.dataobject.Bioassay;
import com.nju.aop.dto.KEAndAO;
import com.nju.aop.repository.BioassayRepository;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.EventRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/findByBioassay")
    public List<KEAndAO> findByBioassay(@RequestParam String bioassay, @RequestParam String effect) {
        String[] bioassays = bioassay.split(",");
        List<String> bioassayList = Arrays.asList(bioassays);
        List<Bioassay> specificBios;
        if(StringUtils.isEmpty(effect)) {
            specificBios = bioassayRepository.findByBioassayNameIn(bioassayList);
        } else {
            specificBios = bioassayRepository.findByBioassayNameInAndEffect(bioassayList, effect);
        }

        List<KEAndAO> list = new ArrayList<>();

        Set<Integer> keIDs = new HashSet<>();
        for(Bioassay bio: specificBios) {
            keIDs.add(bio.getEventId());
        }

        for(int keID: keIDs) {
            KEAndAO keAndAO = new KEAndAO();
            Event KE = eventRepository.getOne(keID);
            keAndAO.setKE(KE);

            Set<Integer> aoIDs = new HashSet<>();
            List<Chain> chainsWithSameEID = chainRepository.findByEventId(keID);
            for(Chain chain:chainsWithSameEID) {
                List<Chain> AOs = chainRepository.findByAopIdAndType(chain.getAopId(),"AdverseOutcome");
                for(Chain ao: AOs) {
                    aoIDs.add(ao.getEventId());
                }
            }

            List<Event> AOs = new ArrayList<>();
            for(int aoID: aoIDs) {
                Event AO = eventRepository.getOne(aoID);
                AOs.add(AO);
            }
            keAndAO.setAOs(AOs);
            list.add(keAndAO);
        }
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

}
