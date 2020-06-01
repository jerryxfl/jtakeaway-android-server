package com.jerry.jtakeaway.requestBean;

public class payBean {
    private String payPassword;
    private int couponId;
    private Integer ordeId;

    public payBean() {
    }

    public payBean(String payPassword, int couponId,int ordeId) {
        this.payPassword = payPassword;
        this.couponId = couponId;
        this.ordeId = ordeId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public Integer getOrdeId() {
        return ordeId;
    }

    public void setOrdeId(Integer ordeId) {
        this.ordeId = ordeId;
    }
}
