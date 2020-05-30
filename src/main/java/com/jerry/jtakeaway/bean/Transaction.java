package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Transaction {
    private int id;
    private Timestamp paytime;
    private BigDecimal paymoney;
    private int userid;
    private String more;
    private Integer couponid;

    @Id
    @Column(name = "ID")
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
    public BigDecimal getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(BigDecimal paymoney) {
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
        Transaction that = (Transaction) o;
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
}
