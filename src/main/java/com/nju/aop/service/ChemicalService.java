package com.nju.aop.service;

import com.nju.aop.dto.ChemicalDTO;

import java.util.List;

/**
 * @author yinywf
 * Created on 2019-11-11
 */
public interface ChemicalService {

    ChemicalDTO findById(Integer id);

    List<ChemicalDTO> findByIds(List<Integer> ids);

    List<ChemicalDTO> findByEvent(Integer eventId);

    List<ChemicalDTO> findByAop(Integer aopId);

    List<ChemicalDTO> findByCas(String cas);

}
