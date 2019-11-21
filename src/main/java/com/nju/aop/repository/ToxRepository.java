package com.nju.aop.repository;


import com.nju.aop.dataobject.Tox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yinywf
 */
public interface ToxRepository extends JpaRepository<Tox, Integer> {

    List<Tox> findByCasrn(String casrn);

    List<Tox> findByToxId(String toxId);

    List<Tox> findByChemical(String chemical);

    List<Tox> findByChemicalLike(String chemical);

    List<Tox> findByAssayName(String assayName);

    List<Tox> findByBioassayLike(String bioassay);

    List<Tox> findByIntendedTargetFamily(String string);
}
