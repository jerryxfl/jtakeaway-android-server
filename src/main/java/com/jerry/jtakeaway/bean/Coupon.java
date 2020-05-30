package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Coupon {
    private int id;
    private String conpondesc;
    private BigDecimal conponprice;
    private Integer conpontarget;
    private Timestamp conponfailuretime;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CONPONDESC")
    public String getConpondesc() {
        return conpondesc;
    }

    public void setConpondesc(String conpondesc) {
        this.conpondesc = conpondesc;
    }

    @Basic
    @Column(name = "CONPONPRICE")
    public BigDecimal getConponprice() {
        return conponprice;
    }

    public void setConponprice(BigDecimal conponprice) {
        this.conponprice = conponprice;
    }

    @Basic
    @Column(name = "CONPONTARGET")
    public Integer getConpontarget() {
        return conpontarget;
    }

    public void setConpontarget(Integer conpontarget) {
        this.conpontarget = conpontarget;
    }

    @Basic
    @Column(name = "CONPONFAILURETIME")
    public Timestamp getConponfailuretime() {
        return conponfailuretime;
    }

    public void setConponfailuretime(Timestamp conponfailuretime) {
        this.conponfailuretime = conponfailuretime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return id == coupon.id &&
                Objects.equals(conpondesc, coupon.conpondesc) &&
                Objects.equals(conponprice, coupon.conponprice) &&
                Objects.equals(conpontarget, coupon.conpontarget) &&
                Objects.equals(conponfailuretime, coupon.conponfailuretime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conpondesc, conponprice, conpontarget, conponfailuretime);
    }
}
