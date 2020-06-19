package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository("HorseDao")
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value="select * from user where ID = ?1",nativeQuery=true)
    User findById(int id);


    User findByAccountAndPassword(String account, String password);
    User findByUsertypeAndUserdetailsid(int userType, int password);
    User findByAccount(String account);
}
