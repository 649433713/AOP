package com.nju.aop.utils.excel;

import com.nju.aop.dataobject.*;
import com.nju.aop.dataobject.importTempVo.BioassayVO;
import com.nju.aop.dataobject.importTempVo.CasEtc;
import com.nju.aop.repository.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.nju.aop.constant.SheetNameConstant.*;

/**
 * @author yinywf
 * Created on 2019-11-07
 */
@Component
public class AopImportUtil {

    public static final String FILE_PATH = "src/main/resources/AOP2.xlsx";

    @Autowired
    private AopRepository aopRepository;

    @Autowired
    private ChainRepository chainRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MieRepository mieRepository;

    @Autowired
    private BiodetectionRepository biodetectionRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private ChemicalRepository chemicalRepository;

    @Autowired
    private ChemicalCasRepository chemicalCasRepository;

    @Autowired
    private ChemicalAopRepository chemicalAopRepository;

    @Autowired
    private ChemicalEventRepository chemicalEventRepository;

    @Autowired
    private BioassayRepository bioassayRepository;

    @Autowired
    private ToxRepository toxRepository;

    @Autowired
    private ChemicalBriefRepository chemicalBriefRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertChains(String path,String name) throws Exception {
        chainRepository.deleteAll();
        List<Chain> list = ExcelUtil.readExcelToEntity(Chain.class, new FileInputStream(path), name,0);
        chainRepository.saveAll(list);
    }
    @Transactional
    public void insertAops(String path,String name) throws Exception {
        aopRepository.deleteAll();
        List<Aop> list = ExcelUtil.readExcelToEntity(Aop.class, new FileInputStream(path), name,1);
        aopRepository.saveAll(list);
    }
    @Transactional
    public void insertEvents(String path,String name) throws Exception {
        eventRepository.deleteAll();
        List<Event> list = ExcelUtil.readExcelToEntity(Event.class, new FileInputStream(path), name,2);
        eventRepository.saveAll(list);
    }
    @Transactional
    public void insertMies(String path,String name) throws Exception {
        mieRepository.deleteAll();
        List<Mie> list = ExcelUtil.readExcelToEntity(Mie.class, new FileInputStream(path), name,3);
        list = list.stream().filter(t -> t.getId() != null).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Mie mie = list.get(i);
            if (mie.getDetectionObjects() == null) {
                mie.setDetectionObjects(list.get(i - 1).getDetectionObjects());
            }
        }
        mieRepository.saveAll(list);
    }
    @Transactional
    public void insertBiodetections(String path,String name) throws Exception {
        biodetectionRepository.deleteAll();
        List<Biodetection> list = ExcelUtil.readExcelToEntity(Biodetection.class, new FileInputStream(path), name,3);
        list = list.stream().filter(t -> t.getName() != null).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Biodetection biodetection = list.get(i);
            if (biodetection.getMieId() == null) {
                biodetection.setMieId(list.get(i - 1).getMieId());
            }
        }
        biodetectionRepository.saveAll(list);
    }
    @Transactional
    public void insertBioassays(String path,String name) throws Exception {
        insertBioassays(new FileInputStream(path), name, 3);
    }
    @Transactional
    void insertBioassays(InputStream inputStream, String name, int sheetNum) throws Exception {
        bioassayRepository.deleteAllInBatch();
        List<BioassayVO> list = ExcelUtil.readExcelToEntity(BioassayVO.class, inputStream, name, sheetNum);
        List<Bioassay> bioassayList = new ArrayList<>();
        for (BioassayVO vo : list) {
            if (vo.getBioassay1() != null) {
                bioassayList.add(new Bioassay(0, vo.getEventId(), vo.getBioassay1(), vo.getEffect()));
            }
            if (vo.getBioassay2() != null) {
                bioassayList.add(new Bioassay(0, vo.getEventId(), vo.getBioassay2(), vo.getEffect()));
            }
        }
        saveBioassays(bioassayList);
    }

    @Transactional
    public void insertToxes(String path,String name)throws Exception {
        toxRepository.deleteAll();
        List<Tox> list = ExcelUtil.readExcelToEntity(Tox.class, new FileInputStream(path), name,4);
        list = list.stream().peek(tox -> {
            while (tox.getBioassay().charAt(tox.getBioassay().length() - 1) == ',') {
                tox.setBioassay(tox.getBioassay().substring(0, tox.getBioassay().length() - 1));
            }
        }).collect(Collectors.toList());

        String sql = "insert into tox (ac50, assay_name, bioassay, casrn, chemical, effect, intended_target_family, tox_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
        List<Tox> finalList = list;
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Tox tox = finalList.get(i);
                preparedStatement.setDouble(1, tox.getAc50());
                preparedStatement.setString(2,tox.getAssayName());
                preparedStatement.setString(3,tox.getBioassay());
                preparedStatement.setString(4,tox.getCasrn());
                preparedStatement.setString(5,tox.getChemical());
                preparedStatement.setString(6,tox.getEffect());
                preparedStatement.setString(7,tox.getIntendedTargetFamily());
                preparedStatement.setString(8,tox.getToxId());
            }

            @Override
            public int getBatchSize() {
                return finalList.size();
            }
        });
    }
    @Transactional
    public void insertChemicals(String path,String name) throws Exception {
        chemicalRepository.deleteAll();
        List<Chemical> list = ExcelUtil.readExcelToEntity(Chemical.class, new FileInputStream(path), name,4);
        chemicalRepository.saveAll(list);
    }
    @Transactional
    public void insertChemicalOtherInfos(String path,String name) throws Exception {
        chemicalCasRepository.deleteAll();
        chemicalAopRepository.deleteAll();
        chemicalEventRepository.deleteAll();
        List<CasEtc> list = ExcelUtil.readExcelToEntity(CasEtc.class, new FileInputStream(path), name,4);
        List<ChemicalCas> caslist = new ArrayList<>();
        List<ChemicalAop> aoplist = new ArrayList<>();
        List<ChemicalEvent> eventlist = new ArrayList<>();

        for (CasEtc casEtc : list) {
            Integer chemicalId = casEtc.getChemicalId();
            if (casEtc.getCas1() != null) {
                caslist.add(new ChemicalCas(null, chemicalId, casEtc.getCas1()));
            }
            if (casEtc.getCas2() != null) {
                caslist.add(new ChemicalCas(null, chemicalId, casEtc.getCas2()));
            }
            if (casEtc.getCas3() != null) {
                caslist.add(new ChemicalCas(null, chemicalId, casEtc.getCas3()));
            }

            Integer aop = casEtc.getAOPID1();
            if (aop != null) {
                aoplist.add(new ChemicalAop(null, chemicalId, aop));
            }
            aop = casEtc.getAOPID2();
            if (aop != null) {
                aoplist.add(new ChemicalAop(null, chemicalId, aop));
            }
            aop = casEtc.getAOPID3();
            if (aop != null) {
                aoplist.add(new ChemicalAop(null, chemicalId, aop));
            }
            aop = casEtc.getAOPID4();
            if (aop != null) {
                aoplist.add(new ChemicalAop(null, chemicalId, aop));
            }
            aop = casEtc.getAOPID5();
            if (aop != null) {
                aoplist.add(new ChemicalAop(null, chemicalId, aop));
            }

            Integer event = casEtc.getIDKE1();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE2();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE3();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE4();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE5();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE6();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE7();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }event = casEtc.getIDKE8();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE9();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE10();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
            event = casEtc.getIDKE11();
            if (event != null) {
                eventlist.add(new ChemicalEvent(null, chemicalId, event));
            }
        }


        chemicalCasRepository.saveAll(caslist);
        chemicalAopRepository.saveAll(aoplist);
        chemicalEventRepository.saveAll(eventlist);
    }
    @Transactional
    public void insertEdges(String path,String name) throws Exception {
        edgeRepository.deleteAll();
        List<Edge> list = ExcelUtil.readExcelToEntity(Edge.class, new FileInputStream(path), name,5);
        edgeRepository.saveAll(list);
    }

    public static void main(String[] args) {
        ///Users/yinywf/Downloads
        String path = "/Users/yinywf/Downloads/AOP汇总表.xlsx";
        try {
            new AopImportUtil().insertAopExcel(new File(path), path.substring(path.indexOf(".xls")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void insertAopExcel(File file, String name) throws Exception {

        Workbook workbook = ExcelUtil.getWorkBoot(new FileInputStream(file), name);
        Iterator<Sheet> iterator = workbook.sheetIterator();
        int sheetNum = 0;
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            String sheetName = sheet.getSheetName();

            switch (sheetName) {
                case CHAIN:
                    chainRepository.deleteAllInBatch();
                    List<Chain> chains = ExcelUtil.readExcelToEntity(Chain.class, new FileInputStream(file), name, sheetNum);
                    saveChains(chains);
                    break;
                case EDGE:
                    edgeRepository.deleteAllInBatch();
                    List<Edge> edges = ExcelUtil.readExcelToEntity(Edge.class, new FileInputStream(file), name, sheetNum);
                    saveEdges(edges);
                    break;
                case AOP:
                    aopRepository.deleteAllInBatch();
                    List<Aop> aops = ExcelUtil.readExcelToEntity(Aop.class, new FileInputStream(file), name, sheetNum);
                    saveAops(aops);
                    break;
                case EVENT:
                    eventRepository.deleteAllInBatch();
                    List<Event> events = ExcelUtil.readExcelToEntity(Event.class, new FileInputStream(file), name, sheetNum);
                    saveEvents(events);
                    insertBioassays(new FileInputStream(file), name, sheetNum);
                    break;
                case MIE:
                    List<MieInteractionType> mieInteractionTypes = ExcelUtil.readExcelToEntity(MieInteractionType.class, new FileInputStream(file), name, sheetNum);
                    Map<Integer, String> mieInteractionTypeMap = mieInteractionTypes.stream().collect(Collectors.toMap(MieInteractionType::getId, mie -> mie.getMieType() == null ? "" : mie.getMieType(), (old, cur) -> old.length() > cur.length() ? old : cur));
                    List<Event> miEvents = new ArrayList<>();
                    for (Map.Entry<Integer,String> entry : mieInteractionTypeMap.entrySet()) {
                        if (!StringUtils.isEmpty(entry.getValue())) {
                            Event event = new Event();
                            event.setId(entry.getKey());
                            event.setMieType(entry.getValue());
                            miEvents.add(event);
                        }
                    }
                    updateEvents(miEvents);
                    break;
                case CHEMICAL:
                    chemicalBriefRepository.deleteAllInBatch();
                    List<ChemicalBrief> chemicalBriefs = ExcelUtil.readExcelToEntity(ChemicalBrief.class, new FileInputStream(file), name, sheetNum);
                    saveChemicalBriefs(chemicalBriefs);
                    break;
            }
            //todo: 判断sheet名字调用插入的方法... ,
            //要求sheet中表头只有一行，表头名称与实体类字段名对应   或  与字段名上@ExcelCell中的名称对应
            sheetNum++;
        }
        //updateChainName
        //update aop.chain set name = (select title from aop.event where id = event_id)
        String sql = "update chain set name = (select title from event where id = event_id)";
        jdbcTemplate.execute(sql);
        sql = "update chain set chinese = (select chinese from event where id = event_id)";
        jdbcTemplate.execute(sql);

    }


    private void saveChemicalBriefs(List<ChemicalBrief> chemicalBriefs) {
        String sql = "insert into chemical_brief (english, chinese, cas, be_in_china) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                ChemicalBrief chemicalBrief = chemicalBriefs.get(i);
                preparedStatement.setString(1, chemicalBrief.getEnglish());
                preparedStatement.setString(2, chemicalBrief.getChinese());
                preparedStatement.setString(3, chemicalBrief.getCas());
                preparedStatement.setByte(4, chemicalBrief.getBeInChina());

            }
            @Override
            public int getBatchSize() {
                return chemicalBriefs.size();
            }
        });
    }

    private void saveBioassays(List<Bioassay> bioassayList) {
        String sql = "insert into bioassay (event_id, bioassay_name, effect) values (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Bioassay bioassay = bioassayList.get(i);
                preparedStatement.setInt(1, bioassay.getEventId());
                preparedStatement.setString(2, bioassay.getBioassayName());
                preparedStatement.setString(3, bioassay.getEffect());
            }
            @Override
            public int getBatchSize() {
                return bioassayList.size();
            }
        });
    }

    private void updateEvents(List<Event> events) {
        String sql = "update event set mie_type = ? where id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Event event = events.get(i);
                preparedStatement.setString(1, event.getMieType());
                preparedStatement.setInt(2, event.getId());
            }

            @Override
            public int getBatchSize() {
                return events.size();
            }
        });
    }
    private void saveEvents(List<Event> events) {
        String sql = "insert into event (id, title, chinese, species, sex, life_cycle, organ, cancer, survival_rates, level, mie_type) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Event event = events.get(i);
                preparedStatement.setInt(1, event.getId());
                preparedStatement.setString(2, event.getTitle());
                preparedStatement.setString(3, event.getChinese());
                preparedStatement.setString(4, event.getSpecies());
                preparedStatement.setString(5, event.getSex());
                preparedStatement.setString(6, event.getLifeCycle());
                preparedStatement.setString(7, event.getOrgan());
                preparedStatement.setString(8, event.getCancer());
                preparedStatement.setString(9, event.getSurvivalRates());
                preparedStatement.setString(10, event.getLevel());
                preparedStatement.setString(11, event.getMieType());
            }

            @Override
            public int getBatchSize() {
                return events.size();
            }
        });
    }

    private void saveChains(List<Chain> chains) {
        String sql = "insert into chain (aop_id, event_id, type, name) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Chain chain = chains.get(i);
                preparedStatement.setInt(1, chain.getAopId());
                preparedStatement.setInt(2, chain.getEventId());
                preparedStatement.setString(3, chain.getType());
                preparedStatement.setString(4, chain.getName());
            }

            @Override
            public int getBatchSize() {
                return chains.size();
            }
        });
    }

    private void saveEdges(List<Edge> edges) {
        String sql = "insert into edge (id, source_id, source_title, target_id, target_title) values (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Edge edge = edges.get(i);
                preparedStatement.setInt(1, edge.getId());
                preparedStatement.setInt(2, edge.getSourceId());
                preparedStatement.setString(3, edge.getSourceTitle());
                preparedStatement.setInt(4, edge.getTargetId());
                preparedStatement.setString(5, edge.getTargetTitle());
            }
            @Override
            public int getBatchSize() {
                return edges.size();
            }
        });
    }

    private void saveAops(List<Aop> aops) {
        String sql = "insert into aop (id, title, chinese, species, sex, life_cycle, organ, cancer, survival_rates, level) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Aop aop = aops.get(i);
                preparedStatement.setInt(1, aop.getId());
                preparedStatement.setString(2, aop.getTitle());
                preparedStatement.setString(3, aop.getChinese());
                preparedStatement.setString(4, aop.getSpecies());
                preparedStatement.setString(5, aop.getSex());
                preparedStatement.setString(6, aop.getLifeCycle());
                preparedStatement.setString(7, aop.getOrgan());
                preparedStatement.setString(8, aop.getCancer());
                preparedStatement.setString(9, aop.getSurvivalRates());
                preparedStatement.setString(10, aop.getLevel());
            }
            @Override
            public int getBatchSize() {
                return aops.size();
            }
        });
    }
    public void insertToxExcel(File file, String name) throws IOException {
        Workbook workbook = ExcelUtil.getWorkBoot(new FileInputStream(file), name);

        Iterator<Sheet> iterator = workbook.sheetIterator();
        while (iterator.hasNext()) {
            Sheet sheet = iterator.next();
            String sheetName = sheet.getSheetName();

            //todo: 判断sheet名字调用插入的方法...
            //要求sheet中表头只有一行，表头名称与实体类字段名对应   或  与字段名上@ExcelCell中的名称对应

        }
    }

}
