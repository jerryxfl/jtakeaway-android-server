package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.OrdeRepository;
import com.jerry.jtakeaway.dao.SlideRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class SlideServiceImp {
    @Resource
    SlideRepository slideRepository;


    public SlideRepository getRepository() {
        return slideRepository;
    }
}
