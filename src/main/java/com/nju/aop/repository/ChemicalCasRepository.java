package com.nju.aop.repository;


import com.nju.aop.dataobject.ChemicalCas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yinywf
 */
public interface ChemicalCasRepository extends JpaRepository<ChemicalCas, Integer> {

    List<ChemicalCas> findByChemicalId(Integer chemicalId);

    List<ChemicalCas> findByChemicalIdIn(List<Integer> chemicalId);

    @Query("select chemicalId from ChemicalCas where cas=:cas")
    List<Integer> findChemicalIdByCas(String cas);
}
