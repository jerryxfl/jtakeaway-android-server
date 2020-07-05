package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Nuser {
    private int id;
    private String address;
    private Integer wallet;

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
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "WALLET")
    public Integer getWallet() {
        return wallet;
    }

    public void setWallet(Integer wallet) {
        this.wallet = wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nuser nuser = (Nuser) o;
        return id == nuser.id&&
                Objects.equals(address, nuser.address) &&
                Objects.equals(wallet, nuser.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, wallet);
    }
}
