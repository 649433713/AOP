package com.nju.aop.repository;


import com.nju.aop.dataobject.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yinywf
 */
public interface EdgeRepository extends JpaRepository<Edge, Integer> {

    List<Edge> findBySourceId(Integer id);

    List<Edge> findByTargetId(Integer id);

    List<Edge> findBySourceTitleLike(String title);

    List<Edge> findByTargetTitleLike(String title);
//    @Query(value = "select * from edge where (source_id,target_id) in (?1)", nativeQuery = true)
    @Query(value = "select * from edge where concat(source_id,' ',target_id) in ?1", nativeQuery = true)
    List<Edge> findBySourceIdAndTargetIdIn(List<String> str);

}
