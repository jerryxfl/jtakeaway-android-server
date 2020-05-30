package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("OrdeDao")
public interface OrdeRepository extends JpaRepository<Orde, Integer> {
}
