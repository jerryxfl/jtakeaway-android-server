package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.SlideRepository;
import com.jerry.jtakeaway.dao.SuserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class SuserServiceImp {
    @Resource
    SuserRepository suserRepository;


    public SuserRepository getRepository() {
        return suserRepository;
    }
}
