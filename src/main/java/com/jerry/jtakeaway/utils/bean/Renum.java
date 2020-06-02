package com.jerry.jtakeaway.utils.bean;

/**
 * @version V1.0
 * @Package com.ss.jwt.R
 * @author: Liu
 * @Date: 10:36
 */
//枚举类
public enum Renum {
    //这里是可以自己定义的，方便与前端交互即可
    UNKNOWN_ERROR(-1,"未知错误"),
    SUCCESS(10000,"成功"),
    USER_NOT_EXIST(1,"用户不存在"),
    USER_IS_EXISTS(2,"用户已在其他设备登录"),
    DATA_IS_NULL(3,"数据为空"),
    PWD_ERROE(4,"密码错误"),
    JWT_FAIL(5,"凭证失效"),
    NO_LOGIN(6,"未登录"),
    PREMS_FAIL(7,"权限不足"),
    NO_WALLTE(8,"你还未开通钱包功能"),
    NO_MONEY(9,"余额不足"),
    NO_ORDE(10,"订单不存在"),
    NO_CONPON(11,"优惠卷不存在"),
    CONPON_FAIL(12,"优惠卷已过期"),
    PAYPAS_FAIL(13,"支付密码错误"),
    CONPON_NO_ZQ(14,"优惠卷不支持此商家"),
    USER_NO_CONPON(15,"您未领取此优惠卷"),
    CANT_SEND_RE(16,"不能在短时间内重复获得发送邮件"),
    S_CODE_FAIL(17,"验证码已失效"),
    S_CODE_ERROR(18,"验证码已错误"),
    OLD_PWD_ERROR(19,"支付密码错误"),
    ;
    private Integer code;
    private String msg;

    Renum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
