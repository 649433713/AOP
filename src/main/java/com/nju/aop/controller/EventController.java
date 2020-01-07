package com.nju.aop.controller;

import com.nju.aop.dataobject.*;
import com.nju.aop.dto.EventWithDistance;
import com.nju.aop.repository.*;
import com.nju.aop.dto.KEAndAO;
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
    @Autowired
    private EdgeRepository edgeRepository;

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

//            Set<Integer> aoIDs = new HashSet<>();
            Map<Integer,Integer> distanceMap = new HashMap<>(); // aoID:the shortest distance between KE(with id keID) and AO(with id aoID)
            List<Chain> chainsWithSameEID = chainRepository.findByEventId(keID);
            for(Chain chain:chainsWithSameEID) {
                List<Chain> AOs = chainRepository.findByAopIdAndType(chain.getAopId(),"AdverseOutcome");
                for(Chain ao: AOs) {
//                    aoIDs.add(ao.getEventId());
                    int distance = getDistance(ao.getAopId(),keID,ao.getEventId());
                    if(!distanceMap.containsKey(ao.getEventId()) || distance < distanceMap.get(ao.getEventId())) {
                        distanceMap.put(ao.getEventId(),distance);
                    }

                }
            }

            List<EventWithDistance> AOs = new ArrayList<>();
//            for(int aoID: aoIDs) {
//                Event AO = eventRepository.getOne(aoID);
//                AOs.add(AO);
//            }
            for(int aoID:distanceMap.keySet()) {
                Event AO = eventRepository.getOne(aoID);
                EventWithDistance eventWithDistance = new EventWithDistance(AO,distanceMap.get(aoID));
                AOs.add(eventWithDistance);
            }
            keAndAO.setAOs(AOs);
            list.add(keAndAO);
        }
        return list;
    }

//    @GetMapping("/distance/{aopId}/{keId}/{aoId}")
    private int getDistance(int aopId, int keId, int aoId) {
        List<Edge> edges = edgeRepository.findByAopId(aopId);
        List<Chain> nodes = chainRepository.findByAopId(aopId);
        int n = nodes.size();
        int[] vertex = new int[n];
        int[][] edgeMatrix = new int[n][n];
        Map<Integer,Integer> map = new HashMap<>();
        int num=0;
        for(Chain node: nodes) {
            map.put(node.getEventId(),num++);
        }
//        System.out.println(map);
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                edgeMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
        for(Edge e: edges) {
            int source = map.get(e.getSourceId());
            int target = map.get(e.getTargetId());
            edgeMatrix[source][target] = 1;
        }

        int[] min = {Integer.MAX_VALUE};
        dfs(map.get(keId),map.get(aoId),0,n,min,vertex,edgeMatrix);
        return min[0];
    }

    private void dfs(int cur, int end, int dis, int n, int[] min, int[] vertex, int[][] edge){
        if(dis>min[0]) return;
        if(cur==end) {
            if(dis < min[0]) {
                min[0] = dis;
                return;
            }
        }
        for (int i = 0; i < n; i++) {
            if (edge[cur][i] != Integer.MAX_VALUE && vertex[i] == 0) {
                vertex[i] = 1;
                dfs(i, end, dis+1,n,min,vertex,edge);
                vertex[i] = 0;
            }
        }
        return;
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
