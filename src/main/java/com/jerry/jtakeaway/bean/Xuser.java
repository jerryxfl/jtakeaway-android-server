package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Xuser {
    private int id;
    private int slideid;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SLIDEID")
    public int getSlideid() {
        return slideid;
    }

    public void setSlideid(int slideid) {
        this.slideid = slideid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Xuser xuser = (Xuser) o;
        return id == xuser.id &&
                slideid == xuser.slideid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slideid);
    }
}
