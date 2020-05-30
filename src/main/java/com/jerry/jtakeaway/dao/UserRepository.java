package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository("HorseDao")
public interface UserRepository extends JpaRepository<User, Integer> {
}
