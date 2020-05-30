package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.MenusRepository;
import com.jerry.jtakeaway.dao.MsgRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class MsgServiceImp {
    @Resource
    MsgRepository msgRepository;


    public MsgRepository getRepository() {
        return msgRepository;
    }
}
