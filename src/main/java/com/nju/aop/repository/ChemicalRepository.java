package com.nju.aop.repository;


import com.nju.aop.dataobject.Chemical;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yinywf
 */
public interface ChemicalRepository extends JpaRepository<Chemical, Integer> {

}
