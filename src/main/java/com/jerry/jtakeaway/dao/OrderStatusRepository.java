package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Orderstatus;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("OrderstatusDao")
public interface OrderStatusRepository extends JpaRepository<Orderstatus, Integer> {
}
