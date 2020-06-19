package com.jerry.jtakeaway.service.imp;

import com.jerry.jtakeaway.dao.AddressRepository;
import com.jerry.jtakeaway.dao.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class CommentServiceImp {
    @Resource
    CommentRepository commentRepository;


    public CommentRepository getRepository() {
        return commentRepository;
    }
}
