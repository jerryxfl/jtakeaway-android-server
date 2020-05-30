package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.UserRepository;
import com.jerry.jtakeaway.dao.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class WalletServiceImp {
    @Resource
    WalletRepository walletRepository;


    public WalletRepository getRepository() {
        return walletRepository;
    }
}
