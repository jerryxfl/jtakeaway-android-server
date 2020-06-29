package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.Loginrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LoginRecordDao")
public interface LoginRecordRepository extends JpaRepository<Loginrecord,Integer> {
    @Query(value ="select * from (select * from loginrecord limit ?1,?2) a where USERID = ?3",nativeQuery= true)
    List<Loginrecord> findByUserid(int start,int end,int userid);
}
