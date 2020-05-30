package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SuserDao")
public interface SuserRepository extends JpaRepository<Suser, Integer> {
}
