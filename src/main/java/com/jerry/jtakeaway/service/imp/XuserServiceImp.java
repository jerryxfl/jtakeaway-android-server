package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.WalletRepository;
import com.jerry.jtakeaway.dao.XUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class XuserServiceImp {
    @Resource
    XUserRepository xUserRepository;


    public XUserRepository getRepository() {
        return xUserRepository;
    }
}
