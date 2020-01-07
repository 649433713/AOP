package com.nju.aop.controller;

import com.nju.aop.dataobject.Aop;
import com.nju.aop.dataobject.Bioassay;
import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Tox;
import com.nju.aop.dto.ChemicalInfo;
import com.nju.aop.repository.BioassayRepository;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.ToxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * created by Kimone
 * date 2019/12/26
 */
@RestController
@Slf4j
@RequestMapping("/api/tox")
public class ToxController {
    @Autowired
    private ToxRepository toxRepository;
    @Autowired
    private ChainRepository chainRepository;
    @Autowired
    private BioassayRepository bioassayRepository;

    @GetMapping("/{queryName}")
    public Page<Tox> getByQueryName(@PathVariable String queryName, Pageable pageable) {
        return toxRepository.findByCasrnOrChemical(queryName,queryName,pageable);
    }

    @PostMapping("/diagnose")
    public Map<Integer, Set<ChemicalInfo>> diagnose(@RequestBody List<Aop> aops) {
        Map<Integer, Set<ChemicalInfo>> map = new HashMap<>();
        List<Integer> aopIDList = aops.stream().map(Aop::getId).collect(Collectors.toList());
        for(int aopId: aopIDList) {
            Set<ChemicalInfo> chemicals = new HashSet<>(); //每一个AOP对应的化学品的集合
            List<Chain> ownedNodes = chainRepository.findByAopId(aopId);
            for(Chain chain: ownedNodes) {
                List<Bioassay> bioassays = bioassayRepository.findByEventId(chain.getEventId());
                for(Bioassay bioassay: bioassays) {
                    List<Tox> toxList;
                    String bioassayName = bioassay.getBioassayName();
                    String effect = bioassay.getEffect();
                    if(StringUtils.isEmpty(effect)) {
                        toxList = toxRepository.findByBioassayOrBioassayLikeOrBioassayLikeOrBioassayLike(
                                bioassayName,"%,"+bioassayName,bioassayName+",%","%,"+bioassayName+",%");
                    }else {
                        toxList = toxRepository.findByBioassayAndEffect(
                                bioassayName,"%,"+bioassayName,bioassayName+",%","%,"+bioassayName+",%",effect);
                    }
                    for(Tox tox: toxList) {
                        chemicals.add(new ChemicalInfo(tox.getCasrn(),tox.getChemical()));
                    }
                }
            }
            map.put(aopId,chemicals);
        }
        return map;
    }
}
