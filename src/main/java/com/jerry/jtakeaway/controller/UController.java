package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
import com.jerry.jtakeaway.dao.XUserRepository;
import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.requestBean.RequestsUser;
import com.jerry.jtakeaway.requestBean.Tmoney;
import com.jerry.jtakeaway.requestBean.changePay;
import com.jerry.jtakeaway.responseBean.ResponseUser;
import com.jerry.jtakeaway.service.imp.*;
import com.jerry.jtakeaway.utils.JwtUtils;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.RedisUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Api(description = "用户信息相关")
@RestController
@RequestMapping("/U")
@SuppressWarnings("all")
public class UController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Resource
    UserServiceImp userServiceImp;

    @Resource
    NuserServiceImp nusersServiceImp;

    @Resource
    SuserServiceImp suserServiceImp;

    @Resource
    HuserServiceImp huserServiceImp;

    @Resource
    XuserServiceImp xuserServiceImp;


    @Resource
    WalletServiceImp walletServiceImp;

    @Resource
    TransactionServiceImp transactionServiceImp;

    @Resource
    JwtUtils jwtUtils;

    @Resource
    RedisUtils redisUtils;

    @ApiOperation("获得用户信息 ")
    @GetMapping("/user_info")
    public Result user_info(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        switch(user.getUsertype()){
            case 0:
                ResponseUser<Nuser> nresponseUser = new ResponseUser<Nuser>();
                nresponseUser.setId(user.getId());
                nresponseUser.setAccount(user.getAccount());
                nresponseUser.setPassword(user.getPassword());
                nresponseUser.setPhone(user.getPhone());
                nresponseUser.setEmail(user.getEmail());
                nresponseUser.setUseradvatar(user.getUseradvatar());
                nresponseUser.setUsernickname(user.getUsernickname());
                nresponseUser.setUsertype(user.getUsertype());
                nresponseUser.setUserdetails(nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null));
                return RUtils.success(nresponseUser);
            case 1:
                ResponseUser<Nuser> sresponseUser = new ResponseUser<Nuser>();
                sresponseUser.setId(user.getId());
                sresponseUser.setAccount(user.getAccount());
                sresponseUser.setPassword(user.getPassword());
                sresponseUser.setPhone(user.getPhone());
                sresponseUser.setEmail(user.getEmail());
                sresponseUser.setUseradvatar(user.getUseradvatar());
                sresponseUser.setUsernickname(user.getUsernickname());
                sresponseUser.setUsertype(user.getUsertype());
                sresponseUser.setUserdetails(nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null));
                return RUtils.success(sresponseUser);
            case 2:
                ResponseUser<Nuser> hresponseUser = new ResponseUser<Nuser>();
                hresponseUser.setId(user.getId());
                hresponseUser.setAccount(user.getAccount());
                hresponseUser.setPassword(user.getPassword());
                hresponseUser.setPhone(user.getPhone());
                hresponseUser.setEmail(user.getEmail());
                hresponseUser.setUseradvatar(user.getUseradvatar());
                hresponseUser.setUsernickname(user.getUsernickname());
                hresponseUser.setUsertype(user.getUsertype());
                hresponseUser.setUserdetails(nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null));
                return RUtils.success(hresponseUser);
            case 3:
                ResponseUser<Nuser> xresponseUser = new ResponseUser<Nuser>();
                xresponseUser.setId(user.getId());
                xresponseUser.setAccount(user.getAccount());
                xresponseUser.setPassword(user.getPassword());
                xresponseUser.setPhone(user.getPhone());
                xresponseUser.setEmail(user.getEmail());
                xresponseUser.setUseradvatar(user.getUseradvatar());
                xresponseUser.setUsernickname(user.getUsernickname());
                xresponseUser.setUsertype(user.getUsertype());
                xresponseUser.setUserdetails(nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null));
                return RUtils.success(xresponseUser);
            default:
                throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        }
    }


    @ApiOperation("修改用户信息 ")
    @PostMapping("/c_user_info")
    public Result c_user_info(HttpServletRequest request, @RequestBody RequestsUser C_user) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());

        user.setPassword(C_user.getPassword());
        user.setEmail(C_user.getEmail());
        user.setPhone(C_user.getPhone());
        user.setUseradvatar(C_user.getUseradvatar());
        user.setUsernickname(C_user.getUsernickname());
        userServiceImp.getRepository().save(user);
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Nuser c_nuser = (Nuser) C_user.getUserdetails();
                nuser.setAddress(c_nuser.getAddress());
                nuser.setPhone(c_nuser.getPhone());
                nusersServiceImp.getRepository().save(nuser);
                return RUtils.success();
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Suser c_suser = (Suser) C_user.getUserdetails();
                suser.setShopname(c_suser.getShopname());
                suser.setShopaddress(c_suser.getShopaddress());
                suser.setShoplicense(c_suser.getShoplicense());
                suser.setIdcard(c_suser.getIdcard());
                suser.setName(c_suser.getName());
                suserServiceImp.getRepository().save(suser);
                return RUtils.success();
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Huser c_huser = (Huser) C_user.getUserdetails();
                huser.setIdcard(c_huser.getIdcard());
                huser.setName(c_huser.getName());
                huser.setTransport(c_huser.getTransport());
                huserServiceImp.getRepository().save(huser);
                return RUtils.success();
            default:
                return RUtils.success();
        }
    }

    @ApiOperation("获得钱包余额 ")
    @GetMapping("/user_wallet_money")
    public Result user_wallet_money(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }
        if(wallet==null){
            return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }else{
            wallet.setPaymentpassword("");
            return RUtils.success(wallet);
        }
    }

    @ApiOperation("充值余额 ")
    @GetMapping("/c_wallet_money")
    public Result c_wallet_money(HttpServletRequest request,int money){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }
        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(money)));
        walletServiceImp.getRepository().save(wallet);
        return RUtils.success();
    }

    @ApiOperation("提现余额 ")
    @PostMapping("/t_wallet_money")
    public Result t_wallet_money(HttpServletRequest request,@RequestBody Tmoney tmoney){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }
        if(wallet.getPaymentpassword().equals(tmoney.getPayPassword())){
            wallet.setBalance(wallet.getBalance().subtract(BigDecimal.valueOf(tmoney.getMoney())));
            walletServiceImp.getRepository().save(wallet);
            return RUtils.success();
        }else{
            return RUtils.Err(Renum.PAYPAS_FAIL.getCode(),Renum.PAYPAS_FAIL.getMsg());
        }

    }


    @ApiOperation("获得交易记录 ")
    @GetMapping("/transactions")
    public Result transactions(HttpServletRequest request)  {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        List<Transaction> transactions = new ArrayList<>();
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }
        if(wallet==null){
            return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }else{
            String[] tIds = wallet.getTransactionid().split(":");
            for (int i = 0; i < tIds.length; i++) {
                Transaction transaction = transactionServiceImp.getRepository().findById(Integer.valueOf(tIds[i])).orElse(null);
                if(transaction!=null)transactions.add(transaction);
            }
            return RUtils.success(transactions);
        }
    }

    // 验证码字符集
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    @ApiOperation("获得修改支付密码验证码 ")
    @GetMapping("/security_code")
    public Result security_code(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        if(redisUtils.get("security_code"+user.getAccount())!=null)return RUtils.Err(Renum.CANT_SEND_RE.getCode(),Renum.CANT_SEND_RE.getMsg());
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int n = ran.nextInt(CHARS.length);
            sb.append(CHARS[n]);
        }
        redisUtils.set("security_code"+user.getAccount(),sb.toString(),60);
        sendMail("1072059168@qq.com","疯狂外卖[支付验证码 🎁]","验证码["+sb.toString()+"]");
        return RUtils.success();
    }

    @Async
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送简单邮件失败");
            throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        }
    }


    @ApiOperation("判断验证码是否正确 ")
    @GetMapping("/p_security_code")
    public Result p_security_code(HttpServletRequest request,String security_code) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        if(redisUtils.get("security_code"+user.getAccount())==null)return RUtils.Err(Renum.S_CODE_FAIL.getCode(),Renum.S_CODE_FAIL.getMsg());
        if(security_code.equalsIgnoreCase(redisUtils.get("security_code"+user.getAccount()).toString()))return RUtils.success();
        else return RUtils.Err(Renum.S_CODE_ERROR.getCode(),Renum.S_CODE_ERROR.getMsg());
    }


    @ApiOperation("修改支付密码 ")
    @PostMapping("/c_security_code")
    public Result c_security_code(HttpServletRequest request,@RequestBody changePay changePay){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        }
        if(wallet==null)return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
        else {
            if(changePay.getOldPayPassword().equals(wallet.getPaymentpassword())){
                wallet.setPaymentpassword(changePay.getNowPayPassword());
                walletServiceImp.getRepository().save(wallet);
                return RUtils.success();
            }else{
                return RUtils.Err(Renum.OLD_PWD_ERROR.getCode(),Renum.OLD_PWD_ERROR.getMsg());
            }
        }
    }



    @ApiOperation("开通钱包 ")
    @GetMapping("/o_wallet")
    public Result o_wallet(HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = new Wallet();
        switch(user.getUsertype()){
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(nuser==null) throw new NullPointerException();
                Wallet wallet1 = walletServiceImp.getRepository().save(wallet);
                nuser.setWallet(wallet1.getId());
                Nuser nuser1 = nusersServiceImp.getRepository().save(nuser);
                return RUtils.success(nuser1);
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(suser==null) throw new NullPointerException();
                Wallet wallet2 = walletServiceImp.getRepository().save(wallet);
                suser.setWalletid(wallet2.getId());
                Suser suser1 = suserServiceImp.getRepository().save(suser);
                return RUtils.success(suser1);
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if(huser==null) throw new NullPointerException();
                Wallet wallet3 =  walletServiceImp.getRepository().save(wallet);
                huser.setWalletid(wallet3.getId());
                Huser huser1 = huserServiceImp.getRepository().save(huser);
                return RUtils.success(huser1);
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(),Renum.NO_WALLTE.getMsg());
            default:
                throw new IllegalArgumentException();
        }
    }



}
