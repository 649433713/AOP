package com.nju.aop.repository;

import com.nju.aop.dataobject.Connectivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * created by Kimone
 * date 2020/1/12
 */
@Repository
public interface ConnectivityRepository extends JpaRepository<Connectivity, Integer> {
}
