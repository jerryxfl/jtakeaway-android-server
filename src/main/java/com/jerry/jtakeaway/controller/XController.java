package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.ServerConfig;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(description = "管理员接口")
@RestController
@RequestMapping("/X")
public class XController {
    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    BroadcastServiceImp broadcastServiceImp;


    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    ApplyServiceImp applyServiceImp;

    @Resource
    WalletServiceImp walletRepository;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    OrdeServiceImp ordeServiceImp;

    @Resource
    SlideServiceImp slideServiceImp;

    @Resource
    XuserServiceImp xuserServiceImp;

    @Resource
    JwtUtils jwtUtils;

    //解析出用户信息
    private Object[] parseSUER(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        Xuser xuser = xuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        Object[] params = new Object[2];
        params[0] = user;
        params[1] = xuser;
        return params;
    }

    @ApiOperation("删除轮播图       slideid")
    @GetMapping("/d_xslide")
    public Result d_slide(HttpServletRequest request,int slideid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        Slide slide = slideServiceImp.getRepository().findById(slideid).orElse(null);
        if(slide==null)throw new NullPointerException();
        if(slide.getUserid()!=user.getId())throw new IllegalArgumentException();
        slideServiceImp.getRepository().delete(slide);
        return RUtils.success();
    }


//    @ApiOperation("添加轮播图    slide")
//    @PostMapping("/a_xslide")
//    public Result a_menu(HttpServletRequest request,@RequestBody Slide slide){
//        Object[] params = parseSUER(request);
//        User user = (User) params[0];
//        Xuser xuser = (Xuser) params[1];
//        if(xuser==null)throw new NullPointerException();
//        Slide saveAndFlush = slideServiceImp.getRepository().saveAndFlush(slide);
//        return RUtils.success(saveAndFlush);
//    }

//    @ApiOperation("修改轮播图       slide")
//    @PostMapping("/alter_xslide")
//    public Result alter_menu(HttpServletRequest request,@RequestBody Slide slide){
//        Object[] params = parseSUER(request);
//        User user = (User) params[0];
//        Xuser xuser = (Xuser) params[1];
//        if(xuser==null)throw new NullPointerException();
//        if(slide==null)throw new NullPointerException();
//        if(slide.getUserid()!=user.getId())throw new IllegalArgumentException();
//        Slide saveAndFlush = slideServiceImp.getRepository().saveAndFlush(slide);
//        return RUtils.success(saveAndFlush);
//    }


    @ApiOperation("添加广播        content")
    @PostMapping("/a_broadcasts")
    public Result a_broadcasts(HttpServletRequest request,@RequestBody String content){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        Broadcasts broadcasts = new Broadcasts();
        broadcasts.setContent(content);
        Broadcasts saveAndFlush = broadcastServiceImp.getRepository().saveAndFlush(broadcasts);
        return RUtils.success(saveAndFlush);
    }



    @ApiOperation("获得一部分申请列表     size")
    @GetMapping("/a_broadcasts")
    public Result a_broadcasts(HttpServletRequest request,int size) {
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];
        if(xuser==null)throw new NullPointerException();
        List<Orde> all = applyServiceImp.getRepository().getAll(size, size + 15);
        return RUtils.success(all);
    }

    @Resource
    HttpServletRequest request;

    @Resource
    HttpSession session;
    @Value(value = "${web.resources-path}")
    private String webResourcesPath;


//    @ApiOperation("上传轮播图片")
//    @PostMapping("/u_xslide")
//    public Result u_slide(@RequestParam("file") MultipartFile mfile) throws IOException {
//        Object[] params = parseSUER(request);
//        User user = (User) params[0];
//        Xuser xuser = (Xuser) params[1];
//
//        System.out.println("有文件上传");
//        if(mfile.isEmpty())throw new NullPointerException();
//
//        String originalFilename = mfile.getOriginalFilename();
//
//        File file = null;
//        Slide slide = null;
//        try{
//            File path = new File(ResourceUtils.getURL("classpath:").getPath());
//            File upload = new File(path.getAbsolutePath(), "static"+File.separator+"slide"+File.separator+user.getAccount()+File.separator);
//            if (!upload.exists()) upload.mkdirs();
//            String uploadPath = upload.getPath() + File.separator;
//            file = new File(uploadPath + originalFilename);
//            mfile.transferTo(file);
//            System.out.println(file.getPath());
//            String remoteaddr = "http://localhost:8080/api-0.1/slide/"+user.getAccount()+"/"+originalFilename;
//            slide = new Slide();
//            slide.setUserid(user.getId());
//            slide.setImg(remoteaddr);
//            slide = slideServiceImp.getRepository().saveAndFlush(slide);
//        }catch(Exception e){
//            throw e;
//        }
//        return RUtils.success(slide);
//    }
    @Resource
    ServerConfig serverConfig;

    @ApiOperation("上传轮播图片")
    @PostMapping("/u_xslide")
    public Result u_slide(@RequestParam("file") MultipartFile file) throws IOException {
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Xuser xuser = (Xuser) params[1];

        System.out.println("有文件上传");
        String fileName = file.getOriginalFilename();
        System.out.println("文件名:"+fileName);

        File dest = new File(webResourcesPath+File.separator+"slide"+File.separator+user.getAccount()+fileName);
        System.out.println("文件路径:"+dest.getPath());

        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try{
            file.transferTo(dest);
            String remoteaddr =serverConfig.getUrl()+"/slide/"+user.getAccount()+"/"+fileName;
            Slide slide = new Slide();
            slide.setUserid(user.getId());
            slide.setImg(remoteaddr);
            slide = slideServiceImp.getRepository().saveAndFlush(slide);
            return RUtils.success(slide);
        }catch (Exception e) {
            return RUtils.Err(Renum.FILE_FAILED.getCode(), Renum.FILE_FAILED.getMsg());
        }
    }

}
