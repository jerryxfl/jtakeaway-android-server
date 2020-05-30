package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Transaction;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("TransactionDao")
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
