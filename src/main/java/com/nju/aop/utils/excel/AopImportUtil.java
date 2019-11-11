package com.nju.aop.utils.excel;

import com.nju.aop.dataobject.*;
import com.nju.aop.dataobject.importTempVo.CasEtc;
import com.nju.aop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinywf
 * Created on 2019-11-07
 */
@Component
public class AopImportUtil {

    public static final String FILE_PATH = "src/main/resources/AOP.xlsx";

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


    public void insertChains() throws Exception {
        List<Chain> list = ExcelUtil.readExcelToEntity(Chain.class, new FileInputStream(FILE_PATH), "AOP.xlsx",0);
        chainRepository.saveAll(list);
    }

    public void insertAops() throws Exception {
        List<Aop> list = ExcelUtil.readExcelToEntity(Aop.class, new FileInputStream(FILE_PATH), "AOP.xlsx",1);
        aopRepository.saveAll(list);
    }

    public void insertEvents() throws Exception {
        List<Event> list = ExcelUtil.readExcelToEntity(Event.class, new FileInputStream(FILE_PATH), "AOP.xlsx",2);
        eventRepository.saveAll(list);
    }

    public void insertMies() throws Exception {
        List<Mie> list = ExcelUtil.readExcelToEntity(Mie.class, new FileInputStream(FILE_PATH), "AOP.xlsx",3);
        list = list.stream().filter(t -> t.getId() != null).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Mie mie = list.get(i);
            if (mie.getDetectionObjects() == null) {
                mie.setDetectionObjects(list.get(i - 1).getDetectionObjects());
            }
        }
        mieRepository.saveAll(list);
    }

    public void insertBiodetections() throws Exception {
        List<Biodetection> list = ExcelUtil.readExcelToEntity(Biodetection.class, new FileInputStream(FILE_PATH), "AOP.xlsx",3);
        list = list.stream().filter(t -> t.getName() != null).collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            Biodetection biodetection = list.get(i);
            if (biodetection.getMieId() == null) {
                biodetection.setMieId(list.get(i - 1).getMieId());
            }
        }
        biodetectionRepository.saveAll(list);
    }

    public void insertChemicals() throws Exception {
        List<Chemical> list = ExcelUtil.readExcelToEntity(Chemical.class, new FileInputStream(FILE_PATH), "AOP.xlsx",4);
        chemicalRepository.saveAll(list);
    }

    public void insertChemicalOtherInfos() throws Exception {
        List<CasEtc> list = ExcelUtil.readExcelToEntity(CasEtc.class, new FileInputStream(FILE_PATH), "AOP.xlsx",4);
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

    public void insertEdges() throws Exception {
        List<Edge> list = ExcelUtil.readExcelToEntity(Edge.class, new FileInputStream(FILE_PATH), "AOP.xlsx",5);
        edgeRepository.saveAll(list);
    }

}
