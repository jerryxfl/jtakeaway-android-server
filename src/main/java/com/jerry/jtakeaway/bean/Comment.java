package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Comment {
    private int id;
    private String content;
    private Timestamp createtime;
    private Suser suser;
    private User user;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "suserid")
    public Suser getSuser() {
        return suser;
    }

    public void setSuser(Suser suser) {
        this.suser = suser;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "userid")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "CREATETIME")
    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id &&
                Objects.equals(content, comment.content) &&
                Objects.equals(createtime, comment.createtime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createtime);
    }
}
