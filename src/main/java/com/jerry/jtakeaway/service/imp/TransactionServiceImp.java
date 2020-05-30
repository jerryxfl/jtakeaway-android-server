package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.SuserRepository;
import com.jerry.jtakeaway.dao.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class TransactionServiceImp {
    @Resource
    TransactionRepository transactionRepository;


    public TransactionRepository getRepository() {
        return transactionRepository;
    }
}
