package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("OrdeDao")
public interface OrdeRepository extends JpaRepository<Orde, Integer> {
    List<Orde> findByNuseridAndSuserid(Integer nuserid, Integer suserid);

}
