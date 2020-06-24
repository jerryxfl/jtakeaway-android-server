package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Slide {
    private int id;
    private int userid;
    private String img;

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
    @Column(name = "IMG")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slide slide = (Slide) o;
        return id == slide.id &&
                userid == slide.userid &&
                Objects.equals(img, slide.img);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userid, img);
    }
}
