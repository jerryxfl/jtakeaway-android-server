package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menus {
    private int id;
    private int suerid;
    private String foodname;
    private String foodimg;
    private String fooddesc;
    private BigDecimal foodprice;
    private BigDecimal foodlowprice;
    private int foodstatus;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SUERID")
    public int getSuerid() {
        return suerid;
    }

    public void setSuerid(int suerid) {
        this.suerid = suerid;
    }

    @Basic
    @Column(name = "FOODNAME")
    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    @Basic
    @Column(name = "FOODIMG")
    public String getFoodimg() {
        return foodimg;
    }

    public void setFoodimg(String foodimg) {
        this.foodimg = foodimg;
    }

    @Basic
    @Column(name = "FOODDESC")
    public String getFooddesc() {
        return fooddesc;
    }

    public void setFooddesc(String fooddesc) {
        this.fooddesc = fooddesc;
    }

    @Basic
    @Column(name = "FOODPRICE")
    public BigDecimal getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(BigDecimal foodprice) {
        this.foodprice = foodprice;
    }

    @Basic
    @Column(name = "FOODLOWPRICE")
    public BigDecimal getFoodlowprice() {
        return foodlowprice;
    }

    public void setFoodlowprice(BigDecimal foodlowprice) {
        this.foodlowprice = foodlowprice;
    }

    @Basic
    @Column(name = "FOODSTATUS")
    public int getFoodstatus() {
        return foodstatus;
    }

    public void setFoodstatus(int foodstatus) {
        this.foodstatus = foodstatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menus menus = (Menus) o;
        return id == menus.id &&
                suerid == menus.suerid &&
                foodstatus == menus.foodstatus &&
                Objects.equals(foodname, menus.foodname) &&
                Objects.equals(foodimg, menus.foodimg) &&
                Objects.equals(fooddesc, menus.fooddesc) &&
                Objects.equals(foodprice, menus.foodprice) &&
                Objects.equals(foodlowprice, menus.foodlowprice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, suerid, foodname, foodimg, fooddesc, foodprice, foodlowprice, foodstatus);
    }
}
