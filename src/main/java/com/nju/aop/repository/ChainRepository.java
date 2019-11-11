package com.nju.aop.repository;


import com.nju.aop.dataobject.Chain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface ChainRepository extends JpaRepository<Chain, Integer> {

    List<Chain> findByAopId(Integer aopId);
}
