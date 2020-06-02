package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Apply;
import com.jerry.jtakeaway.bean.Orde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ApplyDao")
public interface ApplyRepository extends JpaRepository<Apply,Integer> {

    @Query(value ="select * from apply limit ?1,?2",nativeQuery= true)
    List<Orde> getAll(int start, int end);
}
