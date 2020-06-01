package com.jerry.jtakeaway.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Wallet {
    private int id;
    private BigDecimal balance;
    private String paymentpassword;
    private String transactionid;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "BALANCE")
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "PAYMENTPASSWORD")
    public String getPaymentpassword() {
        return paymentpassword;
    }

    public void setPaymentpassword(String paymentpassword) {
        this.paymentpassword = paymentpassword;
    }

    @Basic
    @Column(name = "TRANSACTIONID")
    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id &&
                Objects.equals(balance, wallet.balance) &&
                Objects.equals(paymentpassword, wallet.paymentpassword) &&
                Objects.equals(transactionid, wallet.transactionid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, paymentpassword, transactionid);
    }
}
