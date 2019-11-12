package com.nju.aop.repository;


import com.nju.aop.dataobject.Aop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface AopRepository extends JpaRepository<Aop, Integer> {

    List<Aop> findByTitleLike(String title);

    List<Aop> findByChineseLike(String chinese);

    List<Aop> findByLevel(String level);

    List<Aop> findBySpeciesLike(String species);

    List<Aop> findByLifeCycleLike(String lifeCycle);

    List<Aop> findByOrganLike(String organ);
}
