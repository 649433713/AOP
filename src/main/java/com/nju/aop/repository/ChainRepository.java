package com.nju.aop.repository;


import com.nju.aop.dataobject.Chain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author yinywf
 */
public interface ChainRepository extends JpaRepository<Chain, Integer> {

    List<Chain> findByAopId(Integer aopId);

    List<Chain> findByAopIdAndType(Integer aopId, String type);

    List<Chain> findByEventId(Integer eventId);

    List<Chain> findByEventIdAndType(Integer eventId, String type);

    @Query("select distinct c.eventId from Chain c where c.type=:type")
    List<Integer> findEventIdByType(@Param("type") String type);
}
