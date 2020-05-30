package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Msg {
    private int id;
    private int acceptuserid;
    private int senduserid;
    private String content;
    private Timestamp sendTime;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCEPTUSERID")
    public int getAcceptuserid() {
        return acceptuserid;
    }

    public void setAcceptuserid(int acceptuserid) {
        this.acceptuserid = acceptuserid;
    }

    @Basic
    @Column(name = "SENDUSERID")
    public int getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(int senduserid) {
        this.senduserid = senduserid;
    }

    @Basic
    @Column(name = "CONTENT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "SEND_TIME")
    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Msg msg = (Msg) o;
        return id == msg.id &&
                acceptuserid == msg.acceptuserid &&
                senduserid == msg.senduserid &&
                Objects.equals(content, msg.content) &&
                Objects.equals(sendTime, msg.sendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acceptuserid, senduserid, content, sendTime);
    }
}
