package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.Loginrecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LoginRecordDao")
public interface LoginRecordRepository extends JpaRepository<Loginrecord,Integer> {
    @Query(value="select * from loginrecord where USERID = ?1",nativeQuery = true)
    List<Loginrecord> findByUserid(int userid);
}
