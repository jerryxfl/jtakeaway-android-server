package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.AddressRepository;
import com.jerry.jtakeaway.dao.ApplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class AddressServiceImp {
    @Resource
    AddressRepository addressRepository;


    public AddressRepository getRepository() {
        return addressRepository;
    }
}
