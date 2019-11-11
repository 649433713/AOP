package com.nju.aop.repository;


import com.nju.aop.dataobject.Aop;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yinywf
 */
public interface AopRepository extends JpaRepository<Aop, Integer> {

}
