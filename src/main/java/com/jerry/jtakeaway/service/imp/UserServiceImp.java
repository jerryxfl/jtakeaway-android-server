package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.SuserRepository;
import com.jerry.jtakeaway.dao.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class UserServiceImp {
    @Resource
    UserRepository userRepository;


    public UserRepository getRepository() {
        return userRepository;
    }
}
