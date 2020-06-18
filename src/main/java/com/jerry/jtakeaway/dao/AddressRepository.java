package com.jerry.jtakeaway.dao;

import com.jerry.jtakeaway.bean.Address;
import com.jerry.jtakeaway.bean.Apply;
import com.jerry.jtakeaway.bean.Orde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AddressDao")
public interface AddressRepository extends JpaRepository<Address,Integer> {

}
