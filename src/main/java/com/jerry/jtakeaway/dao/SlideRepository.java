package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Slide;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("SlideDao")
public interface SlideRepository extends JpaRepository<Slide, Integer> {
    List<Slide> findByUserid(int userid);
    Slide findByUseridAndId(int userid,int id);
}
