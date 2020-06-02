package com.jerry.jtakeaway.requestBean;

public class changePay {
    private String oldPayPassword;
    private String nowPayPassword;

    public changePay() {
    }

    public changePay(String oldPayPassword, String nowPayPassword) {
        this.oldPayPassword = oldPayPassword;
        this.nowPayPassword = nowPayPassword;
    }

    public String getOldPayPassword() {
        return oldPayPassword;
    }

    public void setOldPayPassword(String oldPayPassword) {
        this.oldPayPassword = oldPayPassword;
    }

    public String getNowPayPassword() {
        return nowPayPassword;
    }

    public void setNowPayPassword(String nowPayPassword) {
        this.nowPayPassword = nowPayPassword;
    }
}
