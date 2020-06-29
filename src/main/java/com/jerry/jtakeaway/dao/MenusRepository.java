package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Orde;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("MenusDao")
public interface MenusRepository extends JpaRepository<Menus, Integer> {


    @Query(value ="select * from (select * from menus limit ?1,?2) a where SUERID = ?3",nativeQuery= true)
    List<Menus> getAll(int start, int end, int suserid);

    Menus findByIdAndSuerid(int id, int suserid);

    List<Menus> findBySuerid(int suserid);

}
