package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Orderstatus {
    private int id;
    private int statusnum;
    private String statusdesc;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "STATUSNUM")
    public int getStatusnum() {
        return statusnum;
    }

    public void setStatusnum(int statusnum) {
        this.statusnum = statusnum;
    }

    @Basic
    @Column(name = "STATUSDESC")
    public String getStatusdesc() {
        return statusdesc;
    }

    public void setStatusdesc(String statusdesc) {
        this.statusdesc = statusdesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderstatus that = (Orderstatus) o;
        return id == that.id &&
                statusnum == that.statusnum &&
                Objects.equals(statusdesc, that.statusdesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statusnum, statusdesc);
    }
}
