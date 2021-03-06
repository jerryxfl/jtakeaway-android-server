package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.responseBean.ResponseOrder;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Api(description = "商家相关")
@RestController
@SuppressWarnings("all")
@RequestMapping(value = "/S")
public class SController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    UserServiceImp userServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    NuserServiceImp nuserServiceImp;

    @Resource
    WalletServiceImp walletServiceImp;

    @Resource
    MenusServiceImp menusServiceImp;

    @Resource
    ConponServiceImp conponServiceImp;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    OrdeServiceImp ordeServiceImp;

    @Resource
    UserConponServiceImp userConponServiceImp;

    @Resource
    MsgServiceImp msgServiceImp;

    @Resource
    SlideServiceImp slideServiceImp;

    //解析出用户信息
    private Object[] parseSUER(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
        Object[] params = new Object[2];
        params[0] = user;
        params[1] = suser;
        return params;
    }

    @ApiOperation("获得指定商家一部分菜单 size")
    @GetMapping("/g_shops_menus")
    public Result g_shops(HttpServletRequest request, int size){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        List<Menus> menus = new ArrayList<Menus>();
        menus = menusServiceImp.getRepository().getAll(size,size+15,suser.getId());
        return RUtils.success(menus);
    }


    @ApiOperation("下架菜单 菜单id")
    @GetMapping("/off_menu")
    public Result off_menu(HttpServletRequest request, int menuid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        Menus menus = menusServiceImp.getRepository().findById(menuid).orElse(null);
        if(menus==null)throw new NullPointerException();
        menus.setFoodstatus(1);
        menusServiceImp.getRepository().saveAndFlush(menus);
        return RUtils.success();
    }

    @ApiOperation("修改菜单属性")
    @GetMapping("/alter_menu")
    public Result alter_menu(HttpServletRequest request, Menus menus){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        if(menus.getSuerid()!=suser.getId()) throw new IllegalArgumentException();
        menusServiceImp.getRepository().saveAndFlush(menus);
        return RUtils.success(menus);
    }

    @ApiOperation("设置折后价格  lowprice")
    @GetMapping("/menu_lp")
    public Result menu_lp(HttpServletRequest request, int menuid,double lowprice){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        Menus menus = menusServiceImp.getRepository().findByIdAndSuerid(menuid,suser.getId());
        if(menus==null)throw new NullPointerException();
        menus.setFoodlowprice(lowprice);
        Date date  = new Date();
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();
        menus.setLowpricefailed(new Timestamp(date.getTime()));
        menusServiceImp.getRepository().saveAndFlush(menus);
        return RUtils.success(menus);
    }


    @ApiOperation("暂存菜单  menu")
    @GetMapping("/ts_menu")
    public Result ts_menu(HttpServletRequest request,Menus menus){
        menus.setFoodstatus(2);
        Menus saveAndFlush = menusServiceImp.getRepository().saveAndFlush(menus);
        return RUtils.success(saveAndFlush);
    }

    @ApiOperation("上架菜单    menuid")
    @GetMapping("/p_menu")
    public Result p_menu(HttpServletRequest request,int menuid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        Menus menus = menusServiceImp.getRepository().findByIdAndSuerid(menuid,suser.getId());
        if(menus==null) throw new NullPointerException();
        menus.setFoodstatus(0);
        Menus saveAndFlush = menusServiceImp.getRepository().saveAndFlush(menus);
        return RUtils.success(saveAndFlush);
    }



    @ApiOperation("删除商家轮播图       slideid")
    @GetMapping("/d_slide")
    public Result d_slide(HttpServletRequest request,int slideid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        Slide slide = slideServiceImp.getRepository().findById(slideid).orElse(null);
        if(slide==null)throw new NullPointerException();
        if(slide.getUserid()!=user.getId())throw new IllegalArgumentException();
        String[] slides = suser.getSlideid().split(":");
        suser.setSlideid("");
        for (int i = 0; i < slides.length; i++) {
            if(!slides[i].equals(slide.getId())){
                if(suser.getSlideid().equals("")){
                    suser.setSlideid(slides[i]);
                }else{
                    suser.setSlideid(suser.getSlideid()+":"+slides[i]);
                }
            }
        }
        suserServiceImp.getRepository().saveAndFlush(suser);
        slideServiceImp.getRepository().delete(slide);
        return RUtils.success(suser);
    }


    @ApiOperation("获得商家这边一部分的订单     size")
    @GetMapping("/g_sorders")
    public Result g_sorders(HttpServletRequest request,int size){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        List<Orde> ordeList = ordeServiceImp.getRepository().getAllBySuserId(size, size + 15, suser.getId());
        return RUtils.success(getResponseOrders(ordeList,user));
    }


    @ApiOperation("获得商家指定的订单信息     ordeid")
    @GetMapping("/g_sorder")
    public Result g_sorder(HttpServletRequest request,int ordeid){
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
        if(suser==null)throw new NullPointerException();
        Orde orde = ordeServiceImp.getRepository().findById(ordeid).orElse(null);
        if(orde==null)throw new NullPointerException();
        if(orde.getSuserid()!=suser.getId()) throw new IllegalArgumentException();
        ArrayList<Orde> ordes = new ArrayList<>();
        ordes.add(orde);
        return RUtils.success(getResponseOrders(ordes,user));
    }

    @Resource
    HttpServletRequest request;

    @Resource
    HttpSession session;
    @Value(value = "${web.resources-path}")
    private String webResourcesPath;

    @Resource
    ServerConfig serverConfig;
    @ApiOperation("上传轮播图片")
    @PostMapping("/u_sslide")
    public Result u_slide(@RequestParam("file") MultipartFile file) throws IOException {
        Object[] params = parseSUER(request);
        User user = (User) params[0];
        Suser suser = (Suser) params[1];
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
            String remoteaddr = serverConfig.getUrl()+"slide/"+user.getAccount()+"/"+fileName;

            Slide slide = new Slide();
            slide.setUserid(user.getId());
            slide.setImg(remoteaddr);
            slide = slideServiceImp.getRepository().saveAndFlush(slide);
            if(suser.getSlideid()==null||suser.getSlideid().equals("")){
                suser.setSlideid(slide.getId()+"");
            }else{
                suser.setSlideid(suser.getSlideid()+":"+slide.getId());
            }
            suser = suserServiceImp.getRepository().saveAndFlush(suser);
            return RUtils.success(suser);
        }catch (Exception e) {
            return RUtils.Err(Renum.FILE_FAILED.getCode(), Renum.FILE_FAILED.getMsg());
        }
    }


    @Resource
    OrderStatusServiceImp orderStatusServiceImp;


    private List<ResponseOrder> getResponseOrders(List<Orde> orders,User user){
        List<ResponseOrder> responseOrders = new ArrayList<ResponseOrder>();
        for (Orde o:orders) {
            ResponseOrder responseOrder = new ResponseOrder();
            if(o.getHuserid()!= null){
                User huser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(2,o.getHuserid());
                responseOrder.setHuser(huser);
            }
            User nuser = userServiceImp.getRepository().findByUsertypeAndUserdetailsid(0,o.getNuserid());
            responseOrder.setNuser(nuser);
            Orderstatus orderstatus = orderStatusServiceImp.getRepository().findById(o.getStatusid()).orElse(null);
            if(orderstatus!= null){
                responseOrder.setStatus(orderstatus);
            }
            JSONObject menuJsons = JSONObject.parseObject(o.getMenus());
            for (Map.Entry<String, Object> entry : menuJsons.entrySet()) {
                Menus mMenus = menusServiceImp.getRepository().findById(Integer.valueOf(entry.getKey())).orElse(null);
                if(mMenus!= null){
                    responseOrder.setMenus(mMenus);
                }
            }
            responseOrder.setId(o.getId());
            responseOrder.setCreatedTime(o.getCreatedTime());
            responseOrder.setDetailedinformation(o.getDetailedinformation());
            responseOrder.setLevel(o.getLevel());
            responseOrder.setUuid(o.getUuid());
            responseOrder.setSuser(user);
            responseOrders.add(responseOrder);
        }
        return responseOrders;
    }

    private String getServerIPPort(HttpServletRequest request) {
        //+ ":" + request.getServerPort()
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }


}
