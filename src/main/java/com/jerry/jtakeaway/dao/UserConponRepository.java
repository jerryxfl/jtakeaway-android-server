package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Coupon;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("UserConponDao")
public interface UserConponRepository extends JpaRepository<Coupon, Integer> {
}
