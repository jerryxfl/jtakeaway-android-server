package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.MsgRepository;
import com.jerry.jtakeaway.dao.NuserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class NuserServiceImp {
    @Resource
    NuserRepository nuserRepository;


    public NuserRepository getRepository() {
        return nuserRepository;
    }
}
