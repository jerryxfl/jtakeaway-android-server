package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Loginrecord {
    private int id;
    private Timestamp lotintime;
    private String address;
    private User user;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LOTINTIME")
    public Timestamp getLotintime() {
        return lotintime;
    }

    public void setLotintime(Timestamp lotintime) {
        this.lotintime = lotintime;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loginrecord that = (Loginrecord) o;
        return id == that.id &&
                Objects.equals(lotintime, that.lotintime) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lotintime, address);
    }
}
