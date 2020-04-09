package com.nju.aop.controller;

import com.nju.aop.dataobject.*;
import com.nju.aop.dto.EventWithDistance;
import com.nju.aop.repository.*;
import com.nju.aop.dto.KEAndAO;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
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
        saveExcel(list, bioassay, effect);
        return list;
    }

    private void saveExcel(List<KEAndAO> keAndAOList, String bioassay, String effect){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow firstRow = sheet.createRow(0);
        String[] heads = {"KE ID","KE英文名","KE中文名","KE物种","KE性别","KE生命周期","KE器官","KE癌症","KE存活率","KE水平"
                ,"AO ID","AO英文名","AO中文名","AO物种","AO性别","AO生命周期","AO器官","AO癌症","AO存活率","AO水平","距离"};
        XSSFCell[] firstCells = new XSSFCell[heads.length];
        for(int i = 0; i < heads.length; i++) {
            firstCells[i] = firstRow.createCell(i);
            firstCells[i].setCellValue(heads[i]);
        }
        int curRowNum = 1;
        for(KEAndAO keAndAO: keAndAOList) {
            Event ke = keAndAO.getKE();
            List<EventWithDistance> AOs = keAndAO.getAOs();
            if(AOs.size() > 1) {
                CellRangeAddress keId = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,0,0);
                CellRangeAddress keTitle = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,1,1);
                CellRangeAddress keChinese = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1, 2,2);
                CellRangeAddress keSpecies = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,3,3);
                CellRangeAddress keSex = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,4,4);
                CellRangeAddress keCycle = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,5,5);
                CellRangeAddress keOrgan = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,6,6);
                CellRangeAddress keCancer = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,7,7);
                CellRangeAddress keRate = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,8,8);
                CellRangeAddress keLevel = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,9,9);
                sheet.addMergedRegion(keId);
                sheet.addMergedRegion(keTitle);
                sheet.addMergedRegion(keChinese);
                sheet.addMergedRegion(keSpecies);
                sheet.addMergedRegion(keSex);
                sheet.addMergedRegion(keCycle);
                sheet.addMergedRegion(keOrgan);
                sheet.addMergedRegion(keCancer);
                sheet.addMergedRegion(keRate);
                sheet.addMergedRegion(keLevel);
                for(int i=0;i<AOs.size();i++) {
                    EventWithDistance eventWithDistance = AOs.get(i);
                    Event ao = eventWithDistance.getEvent();
                    XSSFRow row = sheet.createRow(curRowNum+i);
                    row.createCell(10).setCellValue(ao.getId());
                    row.createCell(11).setCellValue(ao.getTitle());
                    row.createCell(12).setCellValue(ao.getChinese());
                    row.createCell(13).setCellValue(ao.getSpecies());
                    row.createCell(14).setCellValue(ao.getSex());
                    row.createCell(15).setCellValue(ao.getLifeCycle());
                    row.createCell(16).setCellValue(ao.getOrgan());
                    row.createCell(17).setCellValue(ao.getCancer());
                    row.createCell(18).setCellValue(ao.getSurvivalRates());
                    row.createCell(19).setCellValue(ao.getLevel());
                    row.createCell(20).setCellValue(eventWithDistance.getDistance());
                }
                sheet.getRow(curRowNum).createCell(0).setCellValue(ke.getId());
                sheet.getRow(curRowNum).createCell(1).setCellValue(ke.getTitle());
                sheet.getRow(curRowNum).createCell(2).setCellValue(ke.getChinese());
                sheet.getRow(curRowNum).createCell(3).setCellValue(ke.getSpecies());
                sheet.getRow(curRowNum).createCell(4).setCellValue(ke.getSex());
                sheet.getRow(curRowNum).createCell(5).setCellValue(ke.getLifeCycle());
                sheet.getRow(curRowNum).createCell(6).setCellValue(ke.getOrgan());
                sheet.getRow(curRowNum).createCell(7).setCellValue(ke.getCancer());
                sheet.getRow(curRowNum).createCell(8).setCellValue(ke.getSurvivalRates());
                sheet.getRow(curRowNum).createCell(9).setCellValue(ke.getLevel());
                curRowNum+=AOs.size();
            }
            else {
                XSSFRow row = sheet.createRow(curRowNum++);
                Event ao = AOs.get(0).getEvent();
                row.createCell(0).setCellValue(ke.getId());
                row.createCell(1).setCellValue(ke.getTitle());
                row.createCell(2).setCellValue(ke.getChinese());
                row.createCell(3).setCellValue(ke.getSpecies());
                row.createCell(4).setCellValue(ke.getSex());
                row.createCell(5).setCellValue(ke.getLifeCycle());
                row.createCell(6).setCellValue(ke.getOrgan());
                row.createCell(7).setCellValue(ke.getCancer());
                row.createCell(8).setCellValue(ke.getSurvivalRates());
                row.createCell(9).setCellValue(ke.getLevel());
                row.createCell(10).setCellValue(ao.getId());
                row.createCell(11).setCellValue(ao.getTitle());
                row.createCell(12).setCellValue(ao.getChinese());
                row.createCell(13).setCellValue(ao.getSpecies());
                row.createCell(14).setCellValue(ao.getSex());
                row.createCell(15).setCellValue(ao.getLifeCycle());
                row.createCell(16).setCellValue(ao.getOrgan());
                row.createCell(17).setCellValue(ao.getCancer());
                row.createCell(18).setCellValue(ao.getSurvivalRates());
                row.createCell(19).setCellValue(ao.getLevel());
                row.createCell(20).setCellValue(AOs.get(0).getDistance());
            }

        }

        String name = bioassay+"-"+effect+".xlsx";
        String filePath = File.separator + "tmp" + File.separator + "resource" + File.separator + "static" + File.separator + "resultFiles" + File.separator + name;
//        String filePath = "E:\\resultFiles\\"+name;
        File file = new File(filePath);
        File dir = file.getParentFile();
        if(!dir.exists()) {
            dir.mkdirs();
        }

        if(file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            workbook.write(os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
