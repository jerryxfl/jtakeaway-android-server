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
