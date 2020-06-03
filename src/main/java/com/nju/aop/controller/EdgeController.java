package com.nju.aop.controller;
import com.nju.aop.dataobject.Edge;
import com.nju.aop.dataobject.Event;
import com.nju.aop.dto.EdgeDTO;
import com.nju.aop.repository.EdgeRepository;
import com.nju.aop.repository.EventRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import com.nju.aop.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yinywf
 * Created on 2019-12-11
 */
@RestController
@Slf4j
@RequestMapping("/api/edges")
public class EdgeController {

    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/search/findByExample")
    public Page<EdgeDTO> findByExample(@RequestBody Edge edge, Pageable pageable) {
        Page<Edge> result = edgeRepository.findAll(ExampleMatcherUtil.transfer(edge), pageable);
        List<EdgeDTO> edgeDTOList = transferToDTO(result.getContent());
        return PageUtil.listConvertToPageWithOnePage(edgeDTOList, pageable,result.getTotalElements());
    }

    @PostMapping("/search/findByExample2")
    public Page<Edge> findByExample2(@RequestBody Edge edge, Pageable pageable) {
        return edgeRepository.findAll(ExampleMatcherUtil.transfer(edge), pageable);
    }

    @GetMapping("/search/findByAopId")
    public List<EdgeDTO> findByAopId(@RequestParam Integer aopId) {
        return transferToDTO(edgeRepository.findByAopId(aopId));
    }

    private List<EdgeDTO> transferToDTO(List<Edge> edges) {
        Set<Integer> sourceIdList = edges.stream().map(Edge::getSourceId).collect(Collectors.toSet());
        Set<Integer> targetIdList = edges.stream().map(Edge::getTargetId).collect(Collectors.toSet());
        sourceIdList.addAll(targetIdList);
        List<Event> events = eventRepository.findAllById(sourceIdList);
        Map<Integer, String> eventNameMap = events.stream().collect(Collectors.toMap(Event::getId, Event::getChinese));

        return edges.stream().map(t -> {
            EdgeDTO edgeDTO = new EdgeDTO();
            BeanUtils.copyProperties(t, edgeDTO);
            edgeDTO.setSourceChinese(eventNameMap.get(t.getSourceId()));
            edgeDTO.setTargetChinese(eventNameMap.get(t.getTargetId()));
            return edgeDTO;
        }).collect(Collectors.toList());
    }

    @PostMapping("/findEdgesByEvents")
    public List<Edge> findByEvents(@RequestBody List<Event> events) {
        List<Integer> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        List<Edge> edges = edgeRepository.findBySourceIdInAndTargetIdIn(eventIds, eventIds);
        return edges;
    }
}
