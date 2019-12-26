package com.nju.aop.controller;

import com.nju.aop.dataobject.Tox;
import com.nju.aop.repository.ToxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/{queryName}")
    public Page<Tox> getByQueryName(@PathVariable String queryName, Pageable pageable) {
        return toxRepository.findByCasrnOrChemical(queryName,queryName,pageable);
    }
}
