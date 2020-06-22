package com.nju.aop.repository;

import com.nju.aop.dataobject.ToxCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

/**
 * @author Euterpe on 2020/6/22
 */
public interface ToxCountRepository extends JpaRepository<ToxCount,Integer> {
    List<ToxCount> findByCasrnOrChemical(String casrn, String chemical);
}
