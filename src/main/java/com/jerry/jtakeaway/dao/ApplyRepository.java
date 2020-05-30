package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ApplyDao")
public interface ApplyRepository extends JpaRepository<Apply,Integer> {
}
