package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Msg;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MsgDao")
public interface MsgRepository extends JpaRepository<Msg, Integer> {
}
