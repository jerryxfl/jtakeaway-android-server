package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Jtransaction {
    private int id;
    private Timestamp paytime;
    private Double paymoney;
    private int userid;
    private String more;
    private Integer couponid;
    private int targetuserid;
    private String uuid;

    @Basic
    @Column(name = "TARGETUSERID")
    public int getTargetuserid() {
        return targetuserid;
    }

    public void setTargetuserid(int targetuserid) {
        this.targetuserid = targetuserid;
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
    @Column(name = "PAYTIME")
    public Timestamp getPaytime() {
        return paytime;
    }

    public void setPaytime(Timestamp paytime) {
        this.paytime = paytime;
    }

    @Basic
    @Column(name = "PAYMONEY")
    public Double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(Double paymoney) {
        this.paymoney = paymoney;
    }

    @Basic
    @Column(name = "USERID")
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "MORE")
    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    @Basic
    @Column(name = "COUPONID")
    public Integer getCouponid() {
        return couponid;
    }

    public void setCouponid(Integer couponid) {
        this.couponid = couponid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jtransaction that = (Jtransaction) o;
        return id == that.id &&
                userid == that.userid &&
                Objects.equals(paytime, that.paytime) &&
                Objects.equals(paymoney, that.paymoney) &&
                Objects.equals(more, that.more) &&
                Objects.equals(couponid, that.couponid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paytime, paymoney, userid, more, couponid);
    }

    @Basic
    @Column(name = "UUID")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
