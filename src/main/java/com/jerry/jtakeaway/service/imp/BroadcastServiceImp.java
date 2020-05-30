package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.ApplyRepository;
import com.jerry.jtakeaway.dao.BroadcastsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class BroadcastServiceImp {
    @Resource
    BroadcastsRepository broadcastsRepository;


    public BroadcastsRepository getRepository() {
        return broadcastsRepository;
    }
}
