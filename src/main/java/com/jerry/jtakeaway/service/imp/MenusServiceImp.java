package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.HuserRepository;
import com.jerry.jtakeaway.dao.MenusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class MenusServiceImp {
    @Resource
    MenusRepository menusRepository;


    public MenusRepository getRepository() {
        return menusRepository;
    }
}
