package com.nju.aop.service.impl;

import com.nju.aop.dataobject.Chemical;
import com.nju.aop.dataobject.ChemicalAop;
import com.nju.aop.dataobject.ChemicalCas;
import com.nju.aop.dataobject.ChemicalEvent;
import com.nju.aop.dto.ChemicalDTO;
import com.nju.aop.repository.ChemicalAopRepository;
import com.nju.aop.repository.ChemicalCasRepository;
import com.nju.aop.repository.ChemicalEventRepository;
import com.nju.aop.repository.ChemicalRepository;
import com.nju.aop.service.ChemicalService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
