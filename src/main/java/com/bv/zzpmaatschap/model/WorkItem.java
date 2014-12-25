package com.bv.zzpmaatschap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;

@Entity
@XmlRootElement
@Table(name="workitem")
@NamedQueries(value = {@NamedQuery(name = "selectWorkItem", query = "select w from WorkItem w ")})
public class WorkItem implements Serializable {

    private static final long serialVersionUID = 7613383088966241159L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workMinutes", columnDefinition = "double default 0")
    private double workMinutes = 0.0d;

    @JsonIgnore
    @XmlTransient
    @Transient
    private Date workDate;

    @ManyToOne
    private WorkTariff tariff;


    private String description;

    private String comment;

    public WorkTariff getTariff() {
        return tariff;
    }

    public void setTariff(WorkTariff tariff) {
        this.tariff = tariff;
    }

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		WorkItem other = (WorkItem) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}

    public double getWorkMinutes() {
        return workMinutes;
    }

    public void setWorkMinutes(double workHours) {
        this.workMinutes = workHours;
    }

//	public Item getItem() {
//		return item;
//	}
//
//	public void setItem(Item item) {
//	this.item = item;
//	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @JsonIgnore
    @XmlTransient
    @Transient
    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void copy(WorkItem newWorkItem) {
        newWorkItem.setComment(this.comment);
        newWorkItem.setDescription(this.description);
        newWorkItem.setTariff(this.tariff);
        newWorkItem.setWorkDate(this.workDate);
        newWorkItem.setWorkMinutes(this.workMinutes);
    }
}
