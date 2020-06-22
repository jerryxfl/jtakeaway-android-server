package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Orde {
    private int id;
    private int nuserid;
    private Timestamp createdTime;
    private int suserid;
    private String menus;
    private Integer huserid;
    private int statusid;
    private Double level;
    private String uuid;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NUSERID")
    public int getNuserid() {
        return nuserid;
    }

    public void setNuserid(int nuserid) {
        this.nuserid = nuserid;
    }

    @Basic
    @Column(name = "CREATED_TIME")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "SUSERID")
    public int getSuserid() {
        return suserid;
    }

    public void setSuserid(int suserid) {
        this.suserid = suserid;
    }

    @Basic
    @Column(name = "MENUS")
    public String getMenus() {
        return menus;
    }

    public void setMenus(String menus) {
        this.menus = menus;
    }

    @Basic
    @Column(name = "HUSERID")
    public Integer getHuserid() {
        return huserid;
    }

    public void setHuserid(Integer huserid) {
        this.huserid = huserid;
    }

    @Basic
    @Column(name = "STATUSID")
    public int getStatusid() {
        return statusid;
    }

    public void setStatusid(int statusid) {
        this.statusid = statusid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orde orde = (Orde) o;
        return id == orde.id &&
                nuserid == orde.nuserid &&
                suserid == orde.suserid &&
                statusid == orde.statusid &&
                Objects.equals(createdTime, orde.createdTime) &&
                Objects.equals(menus, orde.menus) &&
                Objects.equals(huserid, orde.huserid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nuserid, createdTime, suserid, menus, huserid, statusid);
    }

    @Basic
    @Column(name = "LEVEL")
    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
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
