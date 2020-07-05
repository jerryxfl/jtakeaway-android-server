package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Suser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SuserDao")
public interface SuserRepository extends JpaRepository<Suser, Integer> {
    @Query(value ="select * from suser limit ?1,?2",nativeQuery= true)
    List<Suser> shopList(int size,int size2);

//    @Query(value ="select * from (select * from suser limit ?1,?2) a where a.LEVEL > ?3",nativeQuery= true)
//    List<Suser> FiveLevelShopList(int size,int size2,int level);

    @Query(value ="select * from suser where LEVEL > ?3 limit ?1,?2",nativeQuery= true)
    List<Suser> FiveLevelShopList(int size,int size2,int level);
}
