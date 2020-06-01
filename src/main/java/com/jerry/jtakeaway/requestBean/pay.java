package com.jerry.jtakeaway.requestBean;

public class pay {
    private String payPassword;
    private Integer ordeId;

    public pay() {
    }

    public pay(String payPassword, Integer ordeId) {
        this.payPassword = payPassword;
        this.ordeId = ordeId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public Integer getOrdeId() {
        return ordeId;
    }

    public void setOrdeId(Integer ordeId) {
        this.ordeId = ordeId;
    }
}
