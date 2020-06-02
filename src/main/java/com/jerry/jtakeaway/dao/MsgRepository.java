package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MsgDao")
public interface MsgRepository extends JpaRepository<Msg, Integer> {

    @Query(value ="select * from (select * from msg limit ?1,?2) a where ACCEPTUSERID = ?3",nativeQuery= true)
    List<Msg> getAll(int start, int end, int nuserid);
}
