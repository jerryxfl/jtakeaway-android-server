package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User {
    private int id;
    private String account;
    private String password;
    private String usernickname;
    private String useradvatar;
    private int usertype;
    private int userdetailsid;
    private String phone;
    private String email;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "USERNICKNAME")
    public String getUsernickname() {
        return usernickname;
    }

    public void setUsernickname(String usernickname) {
        this.usernickname = usernickname;
    }

    @Basic
    @Column(name = "USERADVATAR")
    public String getUseradvatar() {
        return useradvatar;
    }

    public void setUseradvatar(String useradvatar) {
        this.useradvatar = useradvatar;
    }

    @Basic
    @Column(name = "USERTYPE")
    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    @Basic
    @Column(name = "USERDETAILSID")
    public int getUserdetailsid() {
        return userdetailsid;
    }

    public void setUserdetailsid(int userdetailsid) {
        this.userdetailsid = userdetailsid;
    }

    @Basic
    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                usertype == user.usertype &&
                userdetailsid == user.userdetailsid &&
                Objects.equals(account, user.account) &&
                Objects.equals(password, user.password) &&
                Objects.equals(usernickname, user.usernickname) &&
                Objects.equals(useradvatar, user.useradvatar) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, password, usernickname, useradvatar, usertype, userdetailsid, phone, email);
    }
}
