package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.OrdeRepository;
import com.jerry.jtakeaway.dao.OrderStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class OrderStatusServiceImp {
    @Resource
    OrderStatusRepository orderStatusRepository;


    public OrderStatusRepository getRepository() {
        return orderStatusRepository;
    }
}
