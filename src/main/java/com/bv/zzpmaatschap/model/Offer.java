package com.bv.zzpmaatschap.model;


import com.bv.zzpmaatschap.model.report.Report;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

@Entity
@XmlRootElement
@Table(name="offer",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries(value = {@NamedQuery(name = "selectOffer", query = "select o from Offer o where o.name=:name"), @NamedQuery(name = "selectAll", query = "select o from Offer o"), @NamedQuery(name = "getUserOffers", query = "select c.offers from Company c join c.users u where u.id=:user_id")})
public class Offer extends TenantEntity implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 15781704482451L;

    /**
     * Default value included to remove warning. Remove or modify at will. *
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200, message = "Naam moet tussen 1 en 200 tekens zijn.")
    private String name;

    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "offer", fetch = FetchType.LAZY, cascade={CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH})
    private List<Item> items = new ArrayList<Item>();

    @XmlTransient
    @JsonIgnore
    public List<Item> getItems() {
        return items;
    }

    @JsonIgnore
    @XmlTransient
    @OneToMany(mappedBy = "offer", fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    private Set<Report> reports = new HashSet<Report>();

    @Temporal(TemporalType.DATE)
    private Date creationDate=new Date();

    @Size(min = 0, max = 200, message = "Locatie mag maximaal 200 tekens zijn.")
    private String location;

    @Temporal(TemporalType.DATE)
    private Date dateRequest=new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    private Company company;


    private String status;

    private String dateInvoice;

    private String applicant;

    private String address;

    private String email;

    private String nameArchitect;

    private String provisionalUnforeseen;

    private String telephoneCosts;

    private String parkingCosts;

    private String profitAndRisk;
    @JsonProperty(value = "btwwage")
    private String BTWwage;

    @JsonProperty(value = "btwadditional")
    private String BTWadditional;

    public void setItems(List<Item> items) {
        this.items = items;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void copy(Offer newOffer){
        newOffer.setName(this.name+"(kopie)");
        newOffer.setLocation(this.location);
        newOffer.setEmail(this.email);
        newOffer.setApplicant(this.applicant);
        newOffer.setDateInvoice(this.dateInvoice);
        newOffer.setNameArchitect(this.nameArchitect);
        newOffer.setDateRequest(this.dateRequest);
        newOffer.setStatus(this.status);
        newOffer.setCompany(this.company);
        newOffer.setAddress(this.address);
        newOffer.setBTWadditional(this.BTWadditional);
        newOffer.setBTWwage(this.BTWwage);
        newOffer.setTelephoneCosts(this.telephoneCosts);
        newOffer.setProfitAndRisk(this.profitAndRisk);
        newOffer.setProvisionalUnforeseen(this.provisionalUnforeseen);
        newOffer.setParkingCosts(this.parkingCosts);
        newOffer.setCreationDate(new Date());
        for(Item item:this.items){
            Item newItem=new Item();
            newItem.setOffer(newOffer);
            item.copy(newItem);
            newOffer.addItem(newItem);
        }
        this.copyTenant(newOffer);
    }

    private void addItem(final Item item) {
        this.items.add(item);
    }

    public String getNameArchitect() {
        return nameArchitect;
    }

    public void setNameArchitect(String nameArchitect) {
        this.nameArchitect = nameArchitect;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getDateInvoice() {
        return dateInvoice;
    }

    public void setDateInvoice(String dateInvoice) {
        this.dateInvoice = dateInvoice;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getBTWadditional() {
        return BTWadditional;
    }

    public void setBTWadditional(String BTWadditional) {
        this.BTWadditional = BTWadditional;
    }

    public String getBTWwage() {
        return BTWwage;
    }

    public void setBTWwage(String BTWwage) {
        this.BTWwage = BTWwage;
    }

    public String getProfitAndRisk() {
        return profitAndRisk;
    }

    public void setProfitAndRisk(String profitAndRisk) {
        this.profitAndRisk = profitAndRisk;
    }

    public String getParkingCosts() {
        return parkingCosts;
    }

    public void setParkingCosts(String parkingCosts) {
        this.parkingCosts = parkingCosts;
    }

    public String getTelephoneCosts() {
        return telephoneCosts;
    }

    public void setTelephoneCosts(String telephoneCosts) {
        this.telephoneCosts = telephoneCosts;
    }

    public String getProvisionalUnforeseen() {
        return provisionalUnforeseen;
    }

    public void setProvisionalUnforeseen(String provisionalUnforeseen) {
        this.provisionalUnforeseen = provisionalUnforeseen;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonIgnore
    @XmlTransient
    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }


}
