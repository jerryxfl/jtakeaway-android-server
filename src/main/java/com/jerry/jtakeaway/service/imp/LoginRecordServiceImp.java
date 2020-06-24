package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.AddressRepository;
import com.jerry.jtakeaway.dao.LoginRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class LoginRecordServiceImp {
    @Resource
    LoginRecordRepository loginRecordRepository;


    public LoginRecordRepository getRepository() {
        return loginRecordRepository;
    }
}
