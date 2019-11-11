package com.nju.aop.repository;


import com.nju.aop.dataobject.ChemicalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yinywf
 */
public interface ChemicalEventRepository extends JpaRepository<ChemicalEvent, Integer> {

}
