package com.nju.aop.repository;


import com.nju.aop.dataobject.ChemicalAop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yinywf
 */
public interface ChemicalAopRepository extends JpaRepository<ChemicalAop, Integer> {

    List<ChemicalAop> findByChemicalId(Integer chemicalId);

    List<ChemicalAop> findByChemicalIdIn(List<Integer> chemicalId);

    @Query("select chemicalId from ChemicalAop where aopId=:aopId")
    List<Integer> findChemicalIdByAopId(Integer aopId);
}
