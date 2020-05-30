package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.NuserRepository;
import com.jerry.jtakeaway.dao.OrdeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class OrdeServiceImp {
    @Resource
    OrdeRepository ordeRepository;


    public OrdeRepository getRepository() {
        return ordeRepository;
    }
}
