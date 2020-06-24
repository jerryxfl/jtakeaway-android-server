package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Suser {
    private int id;
    private String shopname;
    private String shoplicense;
    private String idcard;
    private String name;
    private String shopaddress;
    private String slideid;
    private Integer applyid;
    private Integer walletid;
    private String dscr;
    private double level;
    private int leveltime;
    private double alllevel;

    @Basic
    @Column(name = "WALLETID")
    public Integer getWalletid() {
        return walletid;
    }

    public void setWalletid(Integer walletid) {
        this.walletid = walletid;
    }

    @Basic
    @Column(name = "APPLYID")
    public Integer getApplyid() {
        return applyid;
    }

    public void setApplyid(Integer applyid) {
        this.applyid = applyid;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SHOPNAME")
    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    @Basic
    @Column(name = "SHOPLICENSE")
    public String getShoplicense() {
        return shoplicense;
    }

    public void setShoplicense(String shoplicense) {
        this.shoplicense = shoplicense;
    }

    @Basic
    @Column(name = "IDCARD")
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "SHOPADDRESS")
    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }

    @Basic
    @Column(name = "SLIDEID")
    public String getSlideid() {
        return slideid;
    }

    public void setSlideid(String slideid) {
        this.slideid = slideid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suser suser = (Suser) o;
        return id == suser.id &&
                Objects.equals(shopname, suser.shopname) &&
                Objects.equals(shoplicense, suser.shoplicense) &&
                Objects.equals(idcard, suser.idcard) &&
                Objects.equals(name, suser.name) &&
                Objects.equals(shopaddress, suser.shopaddress) &&
                Objects.equals(slideid, suser.slideid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopname, shoplicense, idcard, name, shopaddress, slideid);
    }

    @Basic
    @Column(name = "DSCR")
    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
    }

    @Basic
    @Column(name = "LEVEL")
    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    @Basic
    @Column(name = "LEVELTIME")
    public int getLeveltime() {
        return leveltime;
    }

    public void setLeveltime(int leveltime) {
        this.leveltime = leveltime;
    }

    @Basic
    @Column(name = "ALLLEVEL")
    public double getAlllevel() {
        return alllevel;
    }

    public void setAlllevel(double alllevel) {
        this.alllevel = alllevel;
    }
}
