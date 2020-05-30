package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Apply;
import com.jerry.jtakeaway.bean.Broadcasts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("BroadcastsDao")
public interface BroadcastsRepository extends JpaRepository<Broadcasts,Integer> {
}
