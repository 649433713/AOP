package com.nju.aop.repository;


import com.nju.aop.dataobject.Tox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author yinywf
 */
public interface ToxRepository extends JpaRepository<Tox, Integer> {

    List<Tox> findByCasrn(String casrn);

    Page<Tox> findByCasrnOrChemical(String casrn, String chemical, Pageable pageable);

    List<Tox> findByBioassayOrBioassayLikeOrBioassayLikeOrBioassayLike(String bioassay1,String bioassay2,String bioassay3,String bioassay4);

    @Query("select t from Tox t where (t.bioassay like ?1 or bioassay like ?2 or bioassay like ?3 or bioassay like ?4) and effect=?5")
    List<Tox> findByBioassayAndEffect(String bioassay1,String bioassay2,String bioassay3,String bioassay4,String effect);

    List<Tox> findByToxId(String toxId);

    List<Tox> findByChemical(String chemical);

    List<Tox> findByChemicalLike(String chemical);

    List<Tox> findByAssayName(String assayName);

    List<Tox> findByBioassayLike(String bioassay);

    List<Tox> findByIntendedTargetFamily(String string);
}
