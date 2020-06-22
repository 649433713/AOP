package com.nju.aop.repository;


import com.nju.aop.dataobject.ToxCount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yinywf
 */
public interface ToxCountRepository extends JpaRepository<ToxCount, Integer> {

}
