package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Suser implements Serializable {
    private int id;
    private String shopname;
    private String shoplicense;
    private String idcard;
    private String name;
    private String shopaddress;
    private Integer slideid;
    private Integer walletid;
    private int applyid;

    @Id
    @Column(name = "ID")
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
    public Integer getSlideid() {
        return slideid;
    }

    public void setSlideid(Integer slideid) {
        this.slideid = slideid;
    }

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
    public int getApplyid() {
        return applyid;
    }

    public void setApplyid(int applyid) {
        this.applyid = applyid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suser suser = (Suser) o;
        return id == suser.id &&
                applyid == suser.applyid &&
                Objects.equals(shopname, suser.shopname) &&
                Objects.equals(shoplicense, suser.shoplicense) &&
                Objects.equals(idcard, suser.idcard) &&
                Objects.equals(name, suser.name) &&
                Objects.equals(shopaddress, suser.shopaddress) &&
                Objects.equals(slideid, suser.slideid) &&
                Objects.equals(walletid, suser.walletid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopname, shoplicense, idcard, name, shopaddress, slideid, walletid, applyid);
    }
}
