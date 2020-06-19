package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Apply;
import com.jerry.jtakeaway.bean.Comment;
import com.jerry.jtakeaway.bean.Orde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("CommentDao")
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query(value ="select * from comment where  SUSERID = ?1",nativeQuery= true)
    List<Comment> findBySuserid(int suserid);
}
