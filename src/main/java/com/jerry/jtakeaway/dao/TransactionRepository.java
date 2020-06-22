package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Jtransaction;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("TransactionDao")
public interface TransactionRepository extends JpaRepository<Jtransaction, Integer> {
    Jtransaction findByUuid(String uuid);



}
