package com.jerry.jtakeaway.responseBean;

import com.jerry.jtakeaway.bean.Menus;
import com.jerry.jtakeaway.bean.Suser;

import java.awt.*;

public class ShopHaveMenu {
    private Suser suser;
    private Menus menu;

    public ShopHaveMenu() {
    }

    public ShopHaveMenu(Suser suser, Menus menu) {
        this.suser = suser;
        this.menu = menu;
    }

    public Suser getSuser() {
        return suser;
    }

    public void setSuser(Suser suser) {
        this.suser = suser;
    }

    public Menus getMenu() {
        return menu;
    }

    public void setMenu(Menus menu) {
        this.menu = menu;
    }
}
