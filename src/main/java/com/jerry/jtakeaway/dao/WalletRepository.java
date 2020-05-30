package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("WalletDao")
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

}
