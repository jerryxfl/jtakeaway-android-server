package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Huser {
    private int id;
    private String name;
    private String idcard;
    private String transport;
    private Integer walletid;

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
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "IDCARD")
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    @Basic
    @Column(name = "TRANSPORT")
    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Basic
    @Column(name = "WALLETID")
    public Integer getWalletid() {
        return walletid;
    }

    public void setWalletid(Integer walletid) {
        this.walletid = walletid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Huser huser = (Huser) o;
        return id == huser.id &&
                Objects.equals(name, huser.name) &&
                Objects.equals(idcard, huser.idcard) &&
                Objects.equals(transport, huser.transport) &&
                Objects.equals(walletid, huser.walletid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, idcard, transport, walletid);
    }
}
