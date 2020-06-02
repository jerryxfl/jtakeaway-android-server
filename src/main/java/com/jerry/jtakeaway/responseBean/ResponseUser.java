package com.jerry.jtakeaway.responseBean;

public class ResponseUser<T> {
    private int id;
    private String account;
    private String password;
    private String usernickname;
    private String useradvatar;
    private int usertype;
    private T userdetails;
    private String phone;
    private String email;

    public ResponseUser() {
    }

    public ResponseUser(int id, String account, String password, String usernickname, String useradvatar, int usertype, T userdetails, String phone, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.usernickname = usernickname;
        this.useradvatar = useradvatar;
        this.usertype = usertype;
        this.userdetails = userdetails;
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsernickname() {
        return usernickname;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    public String getUseradvatar() {
        return useradvatar;
    }

    public void setUseradvatar(String useradvatar) {
        this.useradvatar = useradvatar;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public T getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(T userdetails) {
        this.userdetails = userdetails;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
