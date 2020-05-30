package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.User;
import com.jerry.jtakeaway.bean.Xuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("XuserDao")
public interface XUserRepository extends JpaRepository<Xuser, Integer> {
}
