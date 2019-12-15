package com.nju.aop.repository;


import com.nju.aop.dataobject.Edge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface EdgeRepository extends JpaRepository<Edge, Integer> {

    List<Edge> findBySourceId(Integer id);

    List<Edge> findByTargetId(Integer id);

    List<Edge> findBySourceTitleLike(String title);

    List<Edge> findByTargetTitleLike(String title);

}
