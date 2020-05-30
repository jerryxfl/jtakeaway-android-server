package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.ConponRepository;
import com.jerry.jtakeaway.dao.HuserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class HuserServiceImp {
    @Resource
    HuserRepository huserRepository;


    public HuserRepository getRepository() {
        return huserRepository;
    }
}
