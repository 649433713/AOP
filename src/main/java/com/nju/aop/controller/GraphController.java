package com.nju.aop.controller;

import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Connectivity;
import com.nju.aop.dataobject.Edge;
import com.nju.aop.dataobject.Event;
import com.nju.aop.dto.EventRank;
import com.nju.aop.dto.RatioInfo;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.ConnectivityRepository;
import com.nju.aop.repository.EdgeRepository;
import com.nju.aop.repository.EventRepository;
import com.nju.aop.utils.GraphUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scala.collection.JavaConverters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by Kimone
 * date 2020/1/10
 */
@RestController
@RequestMapping("/api/graph")
public class GraphController {

    @Autowired
    private ChainRepository chainRepository;
    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ConnectivityRepository connectivityRepository;

    @PostMapping("/importance")
    public List<EventRank> calImportance(@RequestBody RatioInfo ratioInfo) {
        int AOId = ratioInfo.getAoId();
        Event ao = eventRepository.getOne(AOId);

        // get events related to AOId
        Set<Integer> eventIDs = new HashSet<>();
        List<Chain> chainWithAOId = chainRepository.findByEventIdAndType(AOId,"AdverseOutcome");
        for(Chain chain: chainWithAOId) {
            int aopId = chain.getAopId();
            List<Chain> chains = chainRepository.findByAopId(aopId);
            eventIDs.addAll(chains.stream().map(Chain::getEventId).collect(Collectors.toSet()));
        }
        // get edges related to event with id in eventIDs
        List<Integer> eventIDList = new ArrayList<>(eventIDs);//Arrays.asList((Integer[])eventIDs.toArray());
        List<Edge> edges = edgeRepository.findBySourceIdInAndTargetIdIn(eventIDList, eventIDList);

        List<Event> events = eventRepository.findAllById(eventIDs);

       List<EventRank> eventRanks =  GraphUtil.pagerank(JavaConverters.asScalaIteratorConverter(events.iterator()).asScala().toList(),
                JavaConverters.asScalaIteratorConverter(edges.iterator()).asScala().toList());

       for(EventRank eventRank: eventRanks) {
           Event event = eventRepository.getOne(eventRank.getEventId());
           System.out.println(eventRank.toString());
           double score = eventRank.getScore()*ratioInfo.getPageRank();
           if(StringUtils.equals(event.getSpecies(),ao.getSpecies())){
               score += ratioInfo.getSpecies();
           }
           if(StringUtils.equals(event.getSex(),ao.getSex())){
               score += ratioInfo.getSex();
           }
           if(StringUtils.equals(event.getLifeCycle(),ao.getLifeCycle())){
               score += ratioInfo.getLifeCycle();
           }
           if(StringUtils.equals(event.getOrgan(),ao.getOrgan())){
               score += ratioInfo.getOrgan();
           }
           if(StringUtils.equals(event.getCancer(),ao.getCancer())){
               score += ratioInfo.getCancer();
           }
//           if(StringUtils.equals(event.getSurvivalRates(),ao.getSurvivalRates())){
//               score += ratioInfo.getSurvivalRates();
//           }
           if(StringUtils.equals(event.getLevel(),ao.getLevel())){
               score += ratioInfo.getLevel();
           }
           eventRank.setScore(score);
       }
       return eventRanks;
    }

    @GetMapping("/connectedNodes")
    public Map<Integer,List<Integer>> getConnectedNodes() {
        List<Connectivity> list = connectivityRepository.findAll();
        Map<Integer,List<Integer>> map = new HashMap<>();
        for(Connectivity connectivity: list) {
            if(map.get(connectivity.getComponentId())==null) {
                List<Integer> events = new ArrayList<>();
                events.add(connectivity.getEventId());
                map.put(connectivity.getComponentId(), events);
            }else {
                map.get(connectivity.getComponentId()).add(connectivity.getEventId());
            }
        }
        return map;
    }

    @GetMapping("/updateAndGetConnectedNodes")
    public Map<Integer,List<Integer>> updateAndGetConnectedNodes() {
        // delete all records
        connectivityRepository.deleteAll();
        String fileName = "edges.txt";
        String filePath = File.separator + "tmp" + File.separator + "resource" + File.separator + "static" + File.separator + "resultFiles" + File.separator + fileName;
//        String filePath ="E:\\edgesss.txt";
        getEdgesFile(filePath);
        List<List<Object>> nodesList = GraphUtil.connectedNodes(filePath);

        Map<Integer,List<Integer>> map = new HashMap<>();

        int componentId = 1;
        List<Connectivity> list = new ArrayList<>();
        for(List<Object> nodes:nodesList) {
            map.put(componentId,nodes.stream().map(o->Integer.parseInt(o.toString())).collect(Collectors.toList()));
            for(Object o: nodes) {
                Integer eventId = (Integer) o;
                Connectivity connectivity = new Connectivity(componentId,eventId);
                list.add(connectivity);
            }
            componentId++;
        }
        connectivityRepository.saveAll(list);
        return map;
    }

    private void getEdgesFile(String filePath) {
        List<Edge> edges = edgeRepository.findAll();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            for(Edge edge: edges) {
                String str = edge.getSourceId()+" "+edge.getTargetId()+"\r\n";
                bufferedWriter.write(str);
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //必经节点
    @GetMapping("/mustPassNodes/{aoId}")
    public List<Event> calPassNode(@PathVariable Integer aoId) {
        List<Event> events = new ArrayList<>();
        Set<Integer> eventIDs = new HashSet<>();
        List<Chain> chainWithAOId = chainRepository.findByEventIdAndType(aoId,"AdverseOutcome");
        if(chainWithAOId.isEmpty()){
            return events;
        }
        for(Chain chain: chainWithAOId) {
            int aopId = chain.getAopId();
            List<Chain> chains = chainRepository.findByAopId(aopId);
            eventIDs.addAll(chains.stream().map(Chain::getEventId).collect(Collectors.toList()));
        }

        Map<Integer,Integer> map = new HashMap<>();
        List<Node> nodeList = new ArrayList<>();
        int num = 0;
        for(int eventId:eventIDs){
            Node node = new Node(eventId);
            map.put(eventId,num++);
            nodeList.add(node);
        }
        for(Node node:nodeList) {
            int eventId = node.no;
            List<Edge> edges = edgeRepository.findBySourceId(eventId);
            if(edges!=null) {
                for(Edge edge:edges) {
                    int targetId = edge.getTargetId();
                    if(map.get(targetId)!=null) {
                        node.addNext(nodeList.get(map.get(targetId)));
                    }
                }
            }
        }

        for (int i = 0; i < nodeList.size(); i++) {
            nodeList.get(i).mustPassList.addAll(nodeList);
        }
        nodeList = mustPass(nodeList);

        List<Node> mustPass = nodeList.get(map.get(aoId)).mustPassList;
        for(int i=0;i<mustPass.size();i++) {
            System.out.print(mustPass.get(i).no+" ");
        }

        for(Node node: mustPass) {
            events.add(eventRepository.getOne(node.no));
        }
        return events;
    }

    private List<Node> mustPass(List<Node> nodeList) {
        //迭代次数
        int n = 1;
        //判断状态是否稳定Flag
        boolean changed = true;
        while (changed) {
            System.out.println("迭代次数:" + n++);
            changed = false;
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.get(i);
                List<Node> lastMustPassList = new ArrayList<Node>();
                lastMustPassList.addAll(node.mustPassList);
                List<Node> temList = new ArrayList<Node>();
                if(node.preList.size()>0)
                    temList.addAll(node.preList.get(0).mustPassList);
                for(int j=1;j<node.preList.size();j++) {
                    temList.retainAll(node.preList.get(j).mustPassList);
                }
                temList.add(node);
                int lastSize = lastMustPassList.size();
                lastMustPassList.retainAll(temList);
                if (lastSize != lastMustPassList.size()) {
                    node.mustPassList = temList;
                    changed = true;
                }
            }
        }
        return nodeList;
    }

    class Node{
        // 序号
        private int no;
        // 后接节点列表
        private List<Node> nextList = new ArrayList<Node>();
        // 前接节点列表
        private List<Node> preList = new ArrayList<Node>();
        // 初始前必经节点（全体节点）
        private List<Node> mustPassList = new
                ArrayList<Node>();

        private Node(int no) {
            this.no = no;
        }

        private void addNext(Node n){
            nextList.add(n);
            n.preList.add(this);
        }

//        public String toString(){
//            return no+":"+preList.size();
//        }
    }
}
