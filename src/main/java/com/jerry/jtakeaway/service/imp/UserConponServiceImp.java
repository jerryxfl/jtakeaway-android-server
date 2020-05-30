package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.UserConponRepository;
import com.jerry.jtakeaway.dao.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class UserConponServiceImp {
    @Resource
    UserConponRepository userConponRepository;


    public UserConponRepository getRepository() {
        return userConponRepository;
    }
}
