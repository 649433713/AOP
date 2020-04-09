package com.nju.aop.repository;


import com.nju.aop.dataobject.ChemicalBrief;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author yinywf
 */
public interface ChemicalBriefRepository extends JpaRepository<ChemicalBrief, Integer> {
    ChemicalBrief findByCasAndEnglish(String cas, String english);
}
