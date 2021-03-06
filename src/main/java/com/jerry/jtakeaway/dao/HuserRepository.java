package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Huser;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("HuserDao")
public interface HuserRepository extends JpaRepository<Huser, Integer> {
}
