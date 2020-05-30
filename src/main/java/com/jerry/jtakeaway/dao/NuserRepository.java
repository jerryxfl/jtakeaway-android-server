package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Nuser;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("NuserDao")
public interface NuserRepository extends JpaRepository<Nuser, Integer> {
}
