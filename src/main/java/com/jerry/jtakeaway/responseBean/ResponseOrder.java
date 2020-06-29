package com.jerry.jtakeaway.responseBean;



import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Orderstatus;
import com.jerry.jtakeaway.bean.Suser;
import com.jerry.jtakeaway.bean.User;

import java.sql.Timestamp;

public class ResponseOrder {
    private int id;
    private User nuser;
    private Timestamp createdTime;
    private User suser;
    private Suser ssuser;
    private Menus menus;
    private User huser;
    private Orderstatus status;
    private Double level;
    private String uuid;
    private String detailedinformation;
    private int menuSize;

    public ResponseOrder() {
    }

    public ResponseOrder(int id, User nuser, Timestamp createdTime, User suser, Suser ssuser,Menus menus, User huser, Orderstatus status, Double level, String uuid, String detailedinformation,int menuSize) {
        this.id = id;
        this.nuser = nuser;
        this.createdTime = createdTime;
        this.suser = suser;
        this.menus = menus;
        this.huser = huser;
        this.status = status;
        this.level = level;
        this.uuid = uuid;
        this.detailedinformation = detailedinformation;
        this.menuSize = menuSize;
        this.ssuser = ssuser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getNuser() {
        return nuser;
    }

    public void setNuser(User nuser) {
        this.nuser = nuser;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public User getSuser() {
        return suser;
    }

    public void setSuser(User suser) {
        this.suser = suser;
    }

    public Menus getMenus() {
        return menus;
    }

    public void setMenus(Menus menus) {
        this.menus = menus;
    }

    public User getHuser() {
        return huser;
    }

    public void setHuser(User huser) {
        this.huser = huser;
    }

    public Orderstatus getStatus() {
        return status;
    }

    public void setStatus(Orderstatus status) {
        this.status = status;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(int menuSize) {
        this.menuSize = menuSize;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDetailedinformation() {
        return detailedinformation;
    }

    public void setDetailedinformation(String detailedinformation) {
        this.detailedinformation = detailedinformation;
    }

    public Suser getSsuser() {
        return ssuser;
    }

    public void setSsuser(Suser ssuser) {
        this.ssuser = ssuser;
    }
}
