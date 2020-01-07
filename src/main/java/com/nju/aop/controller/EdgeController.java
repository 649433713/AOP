package com.nju.aop.controller;
import com.nju.aop.dataobject.Chain;
import com.nju.aop.dataobject.Edge;
import com.nju.aop.repository.ChainRepository;
import com.nju.aop.repository.EdgeRepository;
import com.nju.aop.utils.ExampleMatcherUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yinywf
 * Created on 2019-12-11
 */
@RestController
@Slf4j
@RequestMapping("/api/edges")
public class EdgeController {

    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private ChainRepository chainRepository;

    @PostMapping("/search/findByExample")
    public Page<Edge> findByExample(@RequestBody Edge edge, Pageable pageable) {
        return edgeRepository.findAll(ExampleMatcherUtil.transfer(edge), pageable);
    }

    @GetMapping("/search/findByExample2")
    public Page<Edge> findByExample2(Edge edge, Pageable pageable) {
        return edgeRepository.findAll(ExampleMatcherUtil.transfer(edge), pageable);
    }

    @GetMapping("/search/findByAopId")
    public List<Edge> findByAopId(@RequestParam Integer aopId) {
//        List<Chain> chains = chainRepository.findByAopId(aopId);
//        List<String> searchList = new ArrayList<>();
//        for (int i = 0; i < chains.size() - 1; i++) {
//            for (int j = 0; j < chains.size(); j++) {
//                if (i == j) {
//                    continue;
//                }
//                searchList.add(chains.get(i).getEventId() + " " + chains.get(j).getEventId());
//            }
//        }
        return edgeRepository.findByAopId(aopId);
    }
}
