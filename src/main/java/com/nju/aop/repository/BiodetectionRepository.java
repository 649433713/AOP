package com.nju.aop.repository;


import com.nju.aop.dataobject.Biodetection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface BiodetectionRepository extends JpaRepository<Biodetection, Integer> {

    List<Biodetection> findByMieId(Integer mieId);

}
