package com.jerry.jtakeaway.bean;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Address {
    private int id;
    private String address;
    private String detaileaddress;
    private String contact;
    private String phone;
    private String label;
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
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "DETAILEADDRESS")
    public String getDetaileaddress() {
        return detaileaddress;
    }

    public void setDetaileaddress(String detaileaddress) {
        this.detaileaddress = detaileaddress;
    }

    @Basic
    @Column(name = "CONTACT")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Basic
    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "LABEL")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return id == address1.id &&
                Objects.equals(address, address1.address) &&
                Objects.equals(detaileaddress, address1.detaileaddress) &&
                Objects.equals(contact, address1.contact) &&
                Objects.equals(phone, address1.phone) &&
                Objects.equals(label, address1.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, detaileaddress, contact, phone, label);
    }
}
