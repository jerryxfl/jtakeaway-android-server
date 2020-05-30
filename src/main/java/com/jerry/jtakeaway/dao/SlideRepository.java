package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Slide;
import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("SlideDao")
public interface SlideRepository extends JpaRepository<Slide, Integer> {
}
