package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ConponDao")
public interface ConponRepository extends JpaRepository<Coupon, Integer> {
}
