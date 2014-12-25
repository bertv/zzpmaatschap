package com.bv.zzpmaatschap.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Cacheable(value = true)
@Entity
@XmlRootElement
@Table(name="company",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries(value = {@NamedQuery(name = "getAllCompanies", query = "select c from Company c"), @NamedQuery(name = "hasUsers", query = "select count(u) from User u inner join u.companies d where d.id=:id")})
public class Company implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 157856867234L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String name;

    @JsonIgnore
    @XmlTransient
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Offer> offers = new HashSet<Offer>();
    @JsonIgnore
    @XmlTransient
    @ManyToMany
    private Set<User> users = new HashSet<User>();

    @Embedded
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @XmlTransient
    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    @JsonIgnore
    @XmlTransient
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (!id.equals(company.id)) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;

        return true;
    }


}