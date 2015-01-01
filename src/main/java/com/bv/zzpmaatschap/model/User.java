package com.bv.zzpmaatschap.model;

import com.bv.zzpmaatschap.rest.CAccount;
import com.bv.zzpmaatschap.rest.CCompanyCheck;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Cacheable
@Entity
@XmlRootElement
@Table(name = "cb_users", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"}))
@NamedQueries(value = {@NamedQuery(name = "getUserByName", query = "select u from User u left join fetch u.companies where u.username=:name"), @NamedQuery(name = "getUserByEmail", query = "select u from User u left join fetch u.companies where u.email=:email and u.password!='test1234567890'"), @NamedQuery(name = "getAllUsers", query = "select u from User u left join fetch u.companies")})
public class User implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 1235423L;

    public User(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(min = 5, max = 50)
    private String username;


    @Size(min = 0, max = 200)
    private String firstname;


    @Size(min = 0, max = 200)
    private String surname;


    @Embedded
    private Address address;

    public static String createEncryptedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] passwordDigest = md.digest();
        return new sun.misc.BASE64Encoder().encode(passwordDigest);
    }

    @JsonIgnore
    @XmlTransient
    @NotNull
    @Size(min = 10)
    private String password;

    @Temporal(TemporalType.DATE)
    @XmlSchemaType(name = "dateTime")
    @NotNull
    private Date creationDate;

    @OneToOne(fetch = FetchType.EAGER)
    private Company defaultCompany;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Company> companies = new HashSet<Company>();

    @OneToOne(fetch = FetchType.EAGER)
    private Role role;

    @PostLoad
    public void checkValidation() throws IllegalAccessException {

    }

    @PrePersist
    public void initialCreationDate() {
        setCreationDate(new Date());
    }

    @NotNull
    @NotEmpty
    @Email
    private String email;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @JsonIgnore
    @XmlTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = createEncryptedPassword(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void setUnencryptedPassword(String password) {
            this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public void transfer(CAccount cuser,List<Company> allCompanies) {
        this.companies.clear();
            for (CCompanyCheck companyCheck: cuser.getCompanyCheck()){
                if (companyCheck.isCheck()){
                    for (Company company:allCompanies){
                        if (companyCheck.getId()==company.getId()){
                        this.companies.add(company);
                        break;
                        }
                    }
                }
            }
        transfer(cuser);
        if (!this.companies.isEmpty()){
            this.defaultCompany=this.companies.iterator().next();
        }
    }
    public void transfer(CAccount cuser) {
        this.address=cuser.getAddress();
        this.creationDate=cuser.getCreationDate();
        this.email=cuser.getEmail();
        this.username=cuser.getUsername();
        this.firstname=cuser.getFirstname();
        this.surname=cuser.getSurname();
        this.role=cuser.getRole();
        this.id=cuser.getId();


    }

    public Company getDefaultCompany() {
        if (defaultCompany==null && companies !=null && !companies.isEmpty()){
            return companies.iterator().next();
        }
        return defaultCompany;
    }

    public void setDefaultCompany(Company defaultCompany) {
        this.defaultCompany = defaultCompany;
    }
}