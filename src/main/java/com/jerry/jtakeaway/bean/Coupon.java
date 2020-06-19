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
    private Double conponprice;
    private Integer conpontarget;
    private Timestamp conponfailuretime;
    private int num;

//    {"conponprice":0.2,"conponfailuretime":"2020-06-04T00:25:57.000+00:00","id":1,"conpondesc":"对所有商家有用"}
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
    public Double getConponprice() {
        return conponprice;
    }

    public void setConponprice(double conponprice) {
        this.conponprice = conponprice;
    }

    public void setConponprice(Double conponprice) {
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

    @Basic
    @Column(name = "NUM")
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
