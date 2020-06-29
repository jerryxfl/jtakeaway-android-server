package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("OrdeDao")
public interface OrdeRepository extends JpaRepository<Orde, Integer> {
    List<Orde> findByNuseridAndSuserid(Integer nuserid, Integer suserid);
    List<Orde> findByNuserid(Integer nuserid);
    List<Orde> findByHuserid(Integer huserid);


    @Query(value ="select * from (select * from orde limit ?1,?2) a where NUSERID = ?3",nativeQuery= true)
    List<Orde> getAll(int start,int end,int nuserid);

    @Query(value ="select * from orde where  NUSERID = ?1 and ID =?2",nativeQuery= true)
    Orde findByNuseridAndId(Integer nuserid, Integer id);

    @Query(value ="select * from (select * from orde limit ?1,?2) a where SUSERID = ?3",nativeQuery= true)
    List<Orde> getAllBySuserId(int start,int end,int suserid);

    Orde findByUuid(String uuid);
}
