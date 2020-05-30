package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.BroadcastsRepository;
import com.jerry.jtakeaway.dao.ConponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class ConponServiceImp {
    @Resource
    ConponRepository conponRepository;


    public ConponRepository getRepository() {
        return conponRepository;
    }
}
