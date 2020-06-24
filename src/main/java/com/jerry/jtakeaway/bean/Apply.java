package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Apply {
    private int id;
    private int userid;
    private String applyreason;
    private Timestamp createtime;
    private int auditstatus;

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
    @Column(name = "USERID")
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "APPLYREASON")
    public String getApplyreason() {
        return applyreason;
    }

    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason;
    }

    @Basic
    @Column(name = "CREATETIME")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Basic
    @Column(name = "AUDITSTATUS")
    public int getAuditstatus() {
        return auditstatus;
    }

    public void setAuditstatus(int auditstatus) {
        this.auditstatus = auditstatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apply apply = (Apply) o;
        return id == apply.id &&
                userid == apply.userid &&
                auditstatus == apply.auditstatus &&
                Objects.equals(applyreason, apply.applyreason) &&
                Objects.equals(createtime, apply.createtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userid, applyreason, createtime, auditstatus);
    }
}
