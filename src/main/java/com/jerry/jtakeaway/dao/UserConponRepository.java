package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.Userconpon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserConponDao")
public interface UserConponRepository extends JpaRepository<Userconpon, Integer> {
    List<Userconpon> findByConponidAndNuserid(int conponid,int nuserid);
    List<Userconpon> findByNuserid(int nuserid);
}
