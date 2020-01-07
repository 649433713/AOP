package com.nju.aop.controller;

import com.nju.aop.dataobject.Aop;
import com.nju.aop.dataobject.Chain;
import com.nju.aop.repository.AopRepository;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yinywf
 * Created on 2019-12-11
 */
@RestController
@Slf4j
@RequestMapping("/api/aops")
public class AopController {

    @Autowired
    private AopRepository aopRepository;
    @Autowired
    private ChainRepository chainRepository;

    @PostMapping("/search/findByExample")
    public Page<Aop> findByExample(@RequestBody Aop aop, Pageable pageable) {

        return aopRepository.findAll(ExampleMatcherUtil.transfer(aop), pageable);
    }

    @GetMapping("/search/findByExample2")
    public Page<Aop> findByExample2(Aop aop, Pageable pageable) {

        return aopRepository.findAll(ExampleMatcherUtil.transfer(aop), pageable);
    }

    @GetMapping("/findByAOId/{AOId}")
    public List<Aop> findByAOId(@PathVariable Integer AOId) {
        List<Chain> chains = chainRepository.findByEventIdAndType(AOId,"AdverseOutcome");
        List<Aop> aops = aopRepository.findAllById(chains.stream().map(Chain::getAopId).collect(Collectors.toSet()));
        return aops;
    }
}
