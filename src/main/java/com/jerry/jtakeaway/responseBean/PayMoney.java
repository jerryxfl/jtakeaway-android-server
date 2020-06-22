package com.jerry.jtakeaway.responseBean;

public class PayMoney {
    private String money;

    public PayMoney(String money) {
        this.money = money;
    }

    public PayMoney() {
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
