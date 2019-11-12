package com.nju.aop.repository;


import com.nju.aop.dataobject.ChemicalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yinywf
 */
public interface ChemicalEventRepository extends JpaRepository<ChemicalEvent, Integer> {

    List<ChemicalEvent> findByChemicalId(Integer chemicalId);

    List<ChemicalEvent> findByChemicalIdIn(List<Integer> chemicalId);

    @Query("select chemicalId from ChemicalEvent where eventId=:eventId")
    List<Integer> findChemicalIdByEventId(Integer eventId);
}
