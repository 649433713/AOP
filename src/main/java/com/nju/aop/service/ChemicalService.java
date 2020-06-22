package com.nju.aop.service;

import com.nju.aop.dto.ChemicalDTO;
import com.nju.aop.dto.KEAndAO;
import com.nju.aop.vo.ToxCollectVO;

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

    List<KEAndAO> findByBioassay(String bioassay, String effect);

    void saveExcel(List<KEAndAO> list, String bioassay, String effect);

    void saveToxExcel(List<ToxCollectVO> ret, String casrn, String chemical);
}
