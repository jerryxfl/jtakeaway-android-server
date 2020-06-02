package com.jerry.jtakeaway.requestBean;

public class Tmoney {
    private double money;
    private String payPassword;

    public Tmoney() {
    }

    public Tmoney(double money, String payPassword) {
        this.money = money;
        this.payPassword = payPassword;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
