package com.jerry.jtakeaway.controller;


import com.alibaba.fastjson.JSONObject;
import com.jerry.jtakeaway.bean.*;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Api(description = "用户信息相关")
@RestController
@RequestMapping("/U")
//@SuppressWarnings("all")
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
    AddressServiceImp addressServiceImp;


    @Resource
    JwtUtils jwtUtils;

    @Resource
    RedisUtils redisUtils;

    @ApiOperation("获得用户信息 ")
    @GetMapping("/user_info")
    public Result user_info(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        switch (user.getUsertype()) {
            case 0:
                ResponseUser nresponseUser = new ResponseUser();
                nresponseUser.setId(user.getId());
                nresponseUser.setAccount(user.getAccount());
                nresponseUser.setPassword(user.getPassword());
                nresponseUser.setPhone(user.getPhone());
                nresponseUser.setEmail(user.getEmail());
                nresponseUser.setUseradvatar(user.getUseradvatar());
                nresponseUser.setUsernickname(user.getUsernickname());
                nresponseUser.setUsertype(user.getUsertype());
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                nresponseUser.setUserdetails(JSONObject.toJSONString(nuser));
                return RUtils.success(nresponseUser);
            case 1:
                ResponseUser sresponseUser = new ResponseUser();
                sresponseUser.setId(user.getId());
                sresponseUser.setAccount(user.getAccount());
                sresponseUser.setPassword(user.getPassword());
                sresponseUser.setPhone(user.getPhone());
                sresponseUser.setEmail(user.getEmail());
                sresponseUser.setUseradvatar(user.getUseradvatar());
                sresponseUser.setUsernickname(user.getUsernickname());
                sresponseUser.setUsertype(user.getUsertype());
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                sresponseUser.setUserdetails(JSONObject.toJSONString(suser));
                return RUtils.success(sresponseUser);
            case 2:
                ResponseUser hresponseUser = new ResponseUser();
                hresponseUser.setId(user.getId());
                hresponseUser.setAccount(user.getAccount());
                hresponseUser.setPassword(user.getPassword());
                hresponseUser.setPhone(user.getPhone());
                hresponseUser.setEmail(user.getEmail());
                hresponseUser.setUseradvatar(user.getUseradvatar());
                hresponseUser.setUsernickname(user.getUsernickname());
                hresponseUser.setUsertype(user.getUsertype());
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                hresponseUser.setUserdetails(JSONObject.toJSONString(huser));
                return RUtils.success(hresponseUser);
            case 3:
                ResponseUser xresponseUser = new ResponseUser();
                xresponseUser.setId(user.getId());
                xresponseUser.setAccount(user.getAccount());
                xresponseUser.setPassword(user.getPassword());
                xresponseUser.setPhone(user.getPhone());
                xresponseUser.setEmail(user.getEmail());
                xresponseUser.setUseradvatar(user.getUseradvatar());
                xresponseUser.setUsernickname(user.getUsernickname());
                xresponseUser.setUsertype(user.getUsertype());
                Xuser xuser = xuserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                xresponseUser.setUserdetails(JSONObject.toJSONString(xuser));
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
        userServiceImp.getRepository().saveAndFlush(user);
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Nuser c_nuser = (Nuser) C_user.getUserdetails();
                nuser.setAddress(c_nuser.getAddress());
                nuser.setPhone(c_nuser.getPhone());
                nusersServiceImp.getRepository().saveAndFlush(nuser);
                return RUtils.success();
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Suser c_suser = (Suser) C_user.getUserdetails();
                suser.setShopname(c_suser.getShopname());
                suser.setShopaddress(c_suser.getShopaddress());
                suser.setShoplicense(c_suser.getShoplicense());
                suser.setIdcard(c_suser.getIdcard());
                suser.setName(c_suser.getName());
                suserServiceImp.getRepository().saveAndFlush(suser);
                return RUtils.success();
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                Huser c_huser = (Huser) C_user.getUserdetails();
                huser.setIdcard(c_huser.getIdcard());
                huser.setName(c_huser.getName());
                huser.setTransport(c_huser.getTransport());
                huserServiceImp.getRepository().saveAndFlush(huser);
                return RUtils.success();
            default:
                return RUtils.success();
        }
    }

    @ApiOperation("获得钱包余额 ")
    @GetMapping("/user_wallet_money")
    public Result user_wallet_money(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        }
        if (wallet == null) {
            return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        } else {
            wallet.setPaymentpassword("");
            return RUtils.success(wallet);
        }
    }

    @ApiOperation("充值余额 ")
    @GetMapping("/c_wallet_money")
    public Result c_wallet_money(HttpServletRequest request, int money) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        }
        wallet.setBalance(wallet.getBalance()+money);
        walletServiceImp.getRepository().saveAndFlush(wallet);
        return RUtils.success();
    }

    @ApiOperation("提现余额 ")
    @PostMapping("/t_wallet_money")
    public Result t_wallet_money(HttpServletRequest request, @RequestBody Tmoney tmoney) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        }
        if (wallet.getPaymentpassword().equals(tmoney.getPayPassword())) {
            if(wallet.getBalance()<tmoney.getMoney())return RUtils.Err(Renum.NO_MONEY.getCode(), Renum.NO_MONEY.getMsg());
            wallet.setBalance(wallet.getBalance()-tmoney.getMoney());
            walletServiceImp.getRepository().saveAndFlush(wallet);
            return RUtils.success();
        } else {
            return RUtils.Err(Renum.PAYPAS_FAIL.getCode(), Renum.PAYPAS_FAIL.getMsg());
        }

    }


    @ApiOperation("获得交易记录 ")
    @GetMapping("/transactions")
    public Result transactions(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        List<Jtransaction> transactions = new ArrayList<>();
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        }
        if (wallet == null) {
            return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        } else {
            String[] tIds = wallet.getTransactionid().split(":");
            for (int i = 0; i < tIds.length; i++) {
                Jtransaction transaction = transactionServiceImp.getRepository().findById(Integer.valueOf(tIds[i])).orElse(null);
                if (transaction != null) transactions.add(transaction);
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
    public Result security_code(HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        if (redisUtils.get("security_code" + user.getAccount()) != null)
            return RUtils.Err(Renum.CANT_SEND_RE.getCode(), Renum.CANT_SEND_RE.getMsg());
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int n = ran.nextInt(CHARS.length);
            sb.append(CHARS[n]);
        }
        redisUtils.set("security_code" + user.getAccount(), sb.toString(), 60);
        sendMail("1072059168@qq.com", "疯狂外卖[支付验证码 🎁]", "验证码[" + sb.toString() + "]");
        return RUtils.success();
    }

    @Async
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送简单邮件失败");
            throw new JException(Renum.UNKNOWN_ERROR.getCode(), Renum.UNKNOWN_ERROR.getMsg());
        }
    }


    @ApiOperation("判断验证码是否正确 ")
    @GetMapping("/p_security_code")
    public Result p_security_code(HttpServletRequest request, String security_code) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        //先判断上一次时效过没有
        if (redisUtils.get("security_code" + user.getAccount()) == null)
            return RUtils.Err(Renum.S_CODE_FAIL.getCode(), Renum.S_CODE_FAIL.getMsg());
        if (security_code.equalsIgnoreCase(redisUtils.get("security_code" + user.getAccount()).toString()))
            return RUtils.success();
        else return RUtils.Err(Renum.S_CODE_ERROR.getCode(), Renum.S_CODE_ERROR.getMsg());
    }


    @ApiOperation("修改支付密码 ")
    @PostMapping("/c_security_code")
    public Result c_security_code(HttpServletRequest request, @RequestBody changePay changePay) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = null;
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(nuser.getWallet()).orElse(null);
                break;
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(suser.getWalletid()).orElse(null);
                break;
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                wallet = walletServiceImp.getRepository().findById(huser.getWalletid()).orElse(null);
                break;
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        }
        if (wallet == null) return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
        else {
            if (changePay.getOldPayPassword().equals(wallet.getPaymentpassword())) {
                wallet.setPaymentpassword(changePay.getNowPayPassword());
                walletServiceImp.getRepository().saveAndFlush(wallet);
                return RUtils.success();
            } else {
                return RUtils.Err(Renum.OLD_PWD_ERROR.getCode(), Renum.OLD_PWD_ERROR.getMsg());
            }
        }
    }


    @ApiOperation("开通钱包 传入支付密码")
    @GetMapping("/o_wallet")
    public Result o_wallet(HttpServletRequest request,String payPassword) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Wallet wallet = new Wallet();
        wallet.setPaymentpassword(payPassword);
        switch (user.getUsertype()) {
            case 0:
                Nuser nuser = nusersServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (nuser == null) throw new NullPointerException();
                Wallet wallet1 = walletServiceImp.getRepository().saveAndFlush(wallet);
                nuser.setWallet(wallet1.getId());
                Nuser nuser1 = nusersServiceImp.getRepository().saveAndFlush(nuser);
                return RUtils.success(nuser1);
            case 1:
                Suser suser = suserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (suser == null) throw new NullPointerException();
                Wallet wallet2 = walletServiceImp.getRepository().saveAndFlush(wallet);
                suser.setWalletid(wallet2.getId());
                Suser suser1 = suserServiceImp.getRepository().saveAndFlush(suser);
                return RUtils.success(suser1);
            case 2:
                Huser huser = huserServiceImp.getRepository().findById(user.getUserdetailsid()).orElse(null);
                if (huser == null) throw new NullPointerException();
                Wallet wallet3 = walletServiceImp.getRepository().saveAndFlush(wallet);
                huser.setWalletid(wallet3.getId());
                Huser huser1 = huserServiceImp.getRepository().saveAndFlush(huser);
                return RUtils.success(huser1);
            case 3:
                return RUtils.Err(Renum.NO_WALLTE.getCode(), Renum.NO_WALLTE.getMsg());
            default:
                throw new IllegalArgumentException();
        }
    }


    @Resource
    HttpServletRequest request;

    @Resource
    HttpSession session;


    @ApiOperation("获取地址")
    @GetMapping("/g_address")
    public Result g_address() {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        List<Address> rAddresses = new ArrayList<Address>();
        List<Address> addresses = new ArrayList<Address>();
        addresses = addressServiceImp.getRepository().findAll();
        for (Address a : addresses) {
            if (a.getUser().getId() == user.getId()) {
                rAddresses.add(a);
            }
        }
        return RUtils.success(rAddresses);
    }

    @ApiOperation("新增地址")
    @PostMapping("/a_address")
    public Result a_address(@RequestBody Address address) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        address.setUser(user);
        Address saveAndFlush = addressServiceImp.getRepository().saveAndFlush(address);
        return RUtils.success(saveAndFlush);
    }

    @ApiOperation("修改地址")
    @PostMapping("/c_address")
    public Result c_address(@RequestBody Address address) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (address.getUser().getId() != user.getId()) throw new IllegalArgumentException();
        Address saveAndFlush = addressServiceImp.getRepository().saveAndFlush(address);
        return RUtils.success(saveAndFlush);
    }

    @ApiOperation("删除地址")
    @PostMapping("/d_address")
    public Result d_address(@RequestBody Address address) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        if (address.getUser().getId() != user.getId()) throw new IllegalArgumentException();
        addressServiceImp.getRepository().delete(address);
        return RUtils.success();
    }

    @ApiOperation("上传头像")
    @GetMapping("/u_advater")
    public Result d_address(@RequestParam("file") MultipartFile mfile) throws IOException {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        System.out.println("有文件上传");
        if(mfile.isEmpty())throw new NullPointerException();

        String originalFilename = mfile.getOriginalFilename();

        File file = null;

        try{
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            File upload = new File(path.getAbsolutePath(), "static/advatar/"+user.getAccount()+"/");
            if (!upload.exists()) upload.mkdirs();
            String uploadPath = upload.getPath() + "\\";
            file = new File(uploadPath + originalFilename);
            mfile.transferTo(file);
            System.out.println(file.getPath());
            String remoteaddr = "http://localhost:8080/api-0.1/advatar/"+user.getAccount()+"/"+originalFilename;
            user.setUseradvatar(remoteaddr);
            userServiceImp.getRepository().saveAndFlush(user);
        }catch(Exception e){
            throw e;
        }
        return RUtils.success();
    }
    @Resource
    CommentServiceImp commentServiceImp;



    @ApiOperation("发布商家评论")
    @GetMapping("/send_shop_comment")
    public Result send_shop_comment(int suserid,String content) {
        String jwt = request.getHeader("jwt");
        Claims claims = jwtUtils.parseJWT(jwt);
        String subject = claims.getSubject();
        JSONObject jsonObject = JSONObject.parseObject(subject);
        User user = userServiceImp.getRepository().findByAccount(JSONObject.toJavaObject(jsonObject, User.class).getAccount());
        Suser suser = suserServiceImp.getRepository().findById(suserid).orElse(null);
        if(suser == null) throw new NullPointerException();
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setSuser(suser);
        comment.setContent(content);
        comment.setCreatetime(new Timestamp(new Date().getTime()));
        Comment saveAndFlush = commentServiceImp.getRepository().saveAndFlush(comment);
        return RUtils.success(saveAndFlush);
    }


}
