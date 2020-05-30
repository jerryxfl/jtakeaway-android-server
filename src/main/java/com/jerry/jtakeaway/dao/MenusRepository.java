package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("MenusDao")
public interface MenusRepository extends JpaRepository<Menus, Integer> {
}
