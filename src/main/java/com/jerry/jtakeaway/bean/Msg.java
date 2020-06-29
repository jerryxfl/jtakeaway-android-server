package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Msg {
    private int id;
    private Integer acceptuserid;
    private Integer senduserid;
    private String content;
    private Timestamp sendTime;
    private int readalready;
    private int pushalready;

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
    @Column(name = "ACCEPTUSERID")
    public Integer getAcceptuserid() {
        return acceptuserid;
    }

    public void setAcceptuserid(Integer acceptuserid) {
        this.acceptuserid = acceptuserid;
    }

    @Basic
    @Column(name = "SENDUSERID")
    public Integer getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(Integer senduserid) {
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

    @Basic
    @Column(name = "READALREADY")
    public int getReadalready() {
        return readalready;
    }

    public void setReadalready(int readalready) {
        this.readalready = readalready;
    }

    @Basic
    @Column(name = "PUSHALREADY")
    public int getPushalready() {
        return pushalready;
    }

    public void setPushalready(int pushalready) {
        this.pushalready = pushalready;
    }
}
