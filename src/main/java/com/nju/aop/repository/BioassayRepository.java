package com.nju.aop.repository;


import com.nju.aop.dataobject.Bioassay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface BioassayRepository extends JpaRepository<Bioassay, Integer> {

    List<Bioassay> findByBioassayName(String name);

    List<Bioassay> findByEventId(Integer id);

    List<Bioassay> findByEffect(String effect);

}
