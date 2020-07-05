package com.jerry.jtakeaway.requestBean;

public class Sign {
    private int type;
    private String userNickName;
    private String password;
    private String idcard;
    private String address;
    private String shopName;
    private String phoneNumber;

    public Sign() {
    }

    public Sign(int type, String userNickName, String password, String idcard, String address, String shopName, String phoneNumber) {
        this.type = type;
        this.userNickName = userNickName;
        this.password = password;
        this.idcard = idcard;
        this.address = address;
        this.shopName = shopName;
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
