package com.nju.aop.service.impl;

import com.nju.aop.dataobject.*;
import com.nju.aop.dto.ChemicalDTO;
import com.nju.aop.dto.EventWithDistance;
import com.nju.aop.dto.KEAndAO;
import com.nju.aop.repository.*;
import com.nju.aop.service.ChemicalService;
import com.nju.aop.utils.MathUtil;
import com.nju.aop.vo.ToxCollectVO;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinywf
 * Created on 2019-11-11
 */
@Service
public class ChemicalServiceImpl implements ChemicalService {


    @Autowired
    private ChemicalAopRepository chemicalAopRepository;

    @Autowired
    private ChemicalRepository chemicalRepository;

    @Autowired
    private ChemicalEventRepository chemicalEventRepository;

    @Autowired
    private ChemicalCasRepository chemicalCasRepository;
    @Autowired
    private BioassayRepository bioassayRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ChainRepository chainRepository;
    @Autowired
    private EdgeRepository edgeRepository;

    @Override
    public ChemicalDTO findById(Integer id) {

        List<ChemicalCas> cases = chemicalCasRepository.findByChemicalId(id);
        List<ChemicalAop> aops = chemicalAopRepository.findByChemicalId(id);
        List<ChemicalEvent> events = chemicalEventRepository.findByChemicalId(id);

        Chemical chemical = chemicalRepository.getOne(id);
        if (chemical == null) {
            return null;
        } else {
            ChemicalDTO chemicalDTO = new ChemicalDTO();
            BeanUtils.copyProperties(chemical, chemicalDTO);
            chemicalDTO.setCas(cases.stream().map(ChemicalCas::getCas).collect(Collectors.toList()));
            chemicalDTO.setAopId(aops.stream().map(ChemicalAop::getAopId).collect(Collectors.toList()));
            chemicalDTO.setEventId(events.stream().map(ChemicalEvent::getEventId).collect(Collectors.toList()));
            return chemicalDTO;
        }
    }

    @Override
    public List<ChemicalDTO> findByIds(List<Integer> ids) {

        List<ChemicalCas> cases = chemicalCasRepository.findByChemicalIdIn(ids);
        List<ChemicalAop> aops = chemicalAopRepository.findByChemicalIdIn(ids);
        List<ChemicalEvent> events = chemicalEventRepository.findByChemicalIdIn(ids);

        Map<Integer, List<ChemicalCas>> casMap = new HashMap<>();
        Map<Integer, List<ChemicalAop>> aopMap = new HashMap<>();
        Map<Integer, List<ChemicalEvent>> eventMap = new HashMap<>();

        for (ChemicalCas chemicalCas : cases) {
            if (casMap.get(chemicalCas.getChemicalId()) == null) {
                List<ChemicalCas> temp = new ArrayList<>();
                temp.add(chemicalCas);
                casMap.put(chemicalCas.getChemicalId(), temp);
            } else {
                casMap.get(chemicalCas.getChemicalId()).add(chemicalCas);
            }
        }
        for (ChemicalAop chemicalAop : aops) {
            if (aopMap.get(chemicalAop.getChemicalId()) == null) {
                List<ChemicalAop> temp = new ArrayList<>();
                temp.add(chemicalAop);
                aopMap.put(chemicalAop.getChemicalId(), temp);
            } else {
                aopMap.get(chemicalAop.getChemicalId()).add(chemicalAop);
            }
        }
        for (ChemicalEvent chemicalEvent : events) {
            if (eventMap.get(chemicalEvent.getChemicalId()) == null) {
                List<ChemicalEvent> temp = new ArrayList<>();
                temp.add(chemicalEvent);
                eventMap.put(chemicalEvent.getChemicalId(), temp);
            } else {
                eventMap.get(chemicalEvent.getChemicalId()).add(chemicalEvent);
            }
        }
        List<ChemicalDTO> result = new ArrayList<>();
        List<Chemical> chemicalList = chemicalRepository.findAllById(ids);
        for (Chemical chemical : chemicalList) {
            ChemicalDTO chemicalDTO = new ChemicalDTO();
            BeanUtils.copyProperties(chemical, chemicalDTO);
            cases = casMap.get(chemical.getId());
            aops = aopMap.get(chemical.getId());
            events = eventMap.get(chemical.getId());
            chemicalDTO.setCas(cases == null ? null : cases.stream().map(ChemicalCas::getCas).collect(Collectors.toList()));
            chemicalDTO.setAopId(aops == null ? null : aops.stream().map(ChemicalAop::getAopId).collect(Collectors.toList()));
            chemicalDTO.setEventId(events == null ? null : events.stream().map(ChemicalEvent::getEventId).collect(Collectors.toList()));
            result.add(chemicalDTO);
        }
        return result;
    }

    @Override
    public List<ChemicalDTO> findByEvent(Integer eventId) {
        return findByIds(chemicalEventRepository.findChemicalIdByEventId(eventId));
    }

    @Override
    public List<ChemicalDTO> findByAop(Integer aopId) {
        return findByIds(chemicalAopRepository.findChemicalIdByAopId(aopId));
    }

    @Override
    public List<ChemicalDTO> findByCas(String cas) {
        return findByIds(chemicalCasRepository.findChemicalIdByCas(cas));
    }

    @Override
    public List<KEAndAO> findByBioassay(String bioassay, String effect) {
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

    @Override
    public void saveExcel(List<KEAndAO> keAndAOList, String bioassay, String effect){
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
                if (AOs.size() > 0) {
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

    @Override
    public void saveToxExcel(List<ToxCollectVO> ret, String casrn, String chemical) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow firstRow = sheet.createRow(0);
        String[] heads = {"化学品名称","CAS号","生物检测名称","生物检测目标","检测效应","检测目标类型","AC50(μM)",
                "KE ID","KE英文名","KE中文名","KE物种","KE性别","KE生命周期","KE器官","KE癌症","KE存活率","KE水平"
                ,"AO ID","AO英文名","AO中文名","AO物种","AO性别","AO生命周期","AO器官","AO癌症","AO存活率","AO水平","距离"};
        XSSFCell[] firstCells = new XSSFCell[heads.length];
        for(int i = 0; i < heads.length; i++) {
            firstCells[i] = firstRow.createCell(i);
            firstCells[i].setCellValue(heads[i]);
        }
        int curRowNum = 1;
        for(ToxCollectVO collectVO : ret) {
            Event ke = collectVO.getKE();
            List<EventWithDistance> AOs = collectVO.getAOs();
            Tox tox = collectVO.getTox();
            if(AOs.size() > 1) {
                CellRangeAddress toxchemical = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,0,0);
                CellRangeAddress toxCAS = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,1,1);
                CellRangeAddress toxAssayName = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,2,2);
                CellRangeAddress toxBioassay = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,3,3);
                CellRangeAddress toxEffect = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,4,4);
                CellRangeAddress toxIntendedTargetFamily = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,5,5);
                CellRangeAddress toxAC50 = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,6,6);
                CellRangeAddress keId = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,7,7);
                CellRangeAddress keTitle = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,8,8);
                CellRangeAddress keChinese = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1, 9,9);
                CellRangeAddress keSpecies = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,10,10);
                CellRangeAddress keSex = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,11,11);
                CellRangeAddress keCycle = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,12,12);
                CellRangeAddress keOrgan = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,13,13);
                CellRangeAddress keCancer = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,14,14);
                CellRangeAddress keRate = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,15,15);
                CellRangeAddress keLevel = new CellRangeAddress(curRowNum,curRowNum+AOs.size()-1,16,16);
                sheet.addMergedRegion(toxAC50);
                sheet.addMergedRegion(toxAssayName);
                sheet.addMergedRegion(toxBioassay);
                sheet.addMergedRegion(toxCAS);
                sheet.addMergedRegion(toxchemical);
                sheet.addMergedRegion(toxEffect);
                sheet.addMergedRegion(toxIntendedTargetFamily);
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
                    row.createCell(17).setCellValue(ao.getId());
                    row.createCell(18).setCellValue(ao.getTitle());
                    row.createCell(19).setCellValue(ao.getChinese());
                    row.createCell(20).setCellValue(ao.getSpecies());
                    row.createCell(21).setCellValue(ao.getSex());
                    row.createCell(22).setCellValue(ao.getLifeCycle());
                    row.createCell(23).setCellValue(ao.getOrgan());
                    row.createCell(24).setCellValue(ao.getCancer());
                    row.createCell(25).setCellValue(ao.getSurvivalRates());
                    row.createCell(26).setCellValue(ao.getLevel());
                    if(AOs.get(0).getDistance() > MathUtil.MAX){
                        row.createCell(27).setCellValue("-");
                    }else {
                        row.createCell(27).setCellValue(AOs.get(0).getDistance());
                    }
                }
                sheet.getRow(curRowNum).createCell(0).setCellValue(tox.getChemical());
                sheet.getRow(curRowNum).createCell(1).setCellValue(tox.getCasrn());
                sheet.getRow(curRowNum).createCell(2).setCellValue(tox.getAssayName());
                sheet.getRow(curRowNum).createCell(3).setCellValue(tox.getBioassay());
                sheet.getRow(curRowNum).createCell(4).setCellValue(tox.getEffect());
                sheet.getRow(curRowNum).createCell(5).setCellValue(tox.getIntendedTargetFamily());
                sheet.getRow(curRowNum).createCell(6).setCellValue(tox.getAc50());
                sheet.getRow(curRowNum).createCell(7).setCellValue(ke.getId());
                sheet.getRow(curRowNum).createCell(8).setCellValue(ke.getTitle());
                sheet.getRow(curRowNum).createCell(9).setCellValue(ke.getChinese());
                sheet.getRow(curRowNum).createCell(10).setCellValue(ke.getSpecies());
                sheet.getRow(curRowNum).createCell(11).setCellValue(ke.getSex());
                sheet.getRow(curRowNum).createCell(12).setCellValue(ke.getLifeCycle());
                sheet.getRow(curRowNum).createCell(13).setCellValue(ke.getOrgan());
                sheet.getRow(curRowNum).createCell(14).setCellValue(ke.getCancer());
                sheet.getRow(curRowNum).createCell(15).setCellValue(ke.getSurvivalRates());
                sheet.getRow(curRowNum).createCell(16).setCellValue(ke.getLevel());
                curRowNum+=AOs.size();
            }
            else {
                if (AOs.size() > 0) {
                    XSSFRow row = sheet.createRow(curRowNum++);
                    Event ao = AOs.get(0).getEvent();
                    row.createCell(0).setCellValue(tox.getChemical());
                    row.createCell(1).setCellValue(tox.getCasrn());
                    row.createCell(2).setCellValue(tox.getAssayName());
                    row.createCell(3).setCellValue(tox.getBioassay());
                    row.createCell(4).setCellValue(tox.getEffect());
                    row.createCell(5).setCellValue(tox.getIntendedTargetFamily());
                    row.createCell(6).setCellValue(tox.getAc50());
                    row.createCell(7).setCellValue(ke.getId());
                    row.createCell(8).setCellValue(ke.getTitle());
                    row.createCell(9).setCellValue(ke.getChinese());
                    row.createCell(10).setCellValue(ke.getSpecies());
                    row.createCell(11).setCellValue(ke.getSex());
                    row.createCell(12).setCellValue(ke.getLifeCycle());
                    row.createCell(13).setCellValue(ke.getOrgan());
                    row.createCell(14).setCellValue(ke.getCancer());
                    row.createCell(15).setCellValue(ke.getSurvivalRates());
                    row.createCell(16).setCellValue(ke.getLevel());
                    row.createCell(17).setCellValue(ao.getId());
                    row.createCell(18).setCellValue(ao.getTitle());
                    row.createCell(19).setCellValue(ao.getChinese());
                    row.createCell(20).setCellValue(ao.getSpecies());
                    row.createCell(21).setCellValue(ao.getSex());
                    row.createCell(22).setCellValue(ao.getLifeCycle());
                    row.createCell(23).setCellValue(ao.getOrgan());
                    row.createCell(24).setCellValue(ao.getCancer());
                    row.createCell(25).setCellValue(ao.getSurvivalRates());
                    row.createCell(26).setCellValue(ao.getLevel());
                    if(AOs.get(0).getDistance() > MathUtil.MAX){
                        row.createCell(27).setCellValue("-");
                    }else {
                        row.createCell(27).setCellValue(AOs.get(0).getDistance());
                    }
                }
            }
        }

        String name = casrn+"-"+chemical+".xlsx";
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
}
