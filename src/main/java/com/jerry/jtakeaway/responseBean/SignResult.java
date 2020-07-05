package com.jerry.jtakeaway.responseBean;

public class SignResult {
    private String account;

    public SignResult() {
    }

    public SignResult( String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
