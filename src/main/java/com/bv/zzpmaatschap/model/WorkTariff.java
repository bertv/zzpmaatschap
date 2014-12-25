package com.bv.zzpmaatschap.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name="worktariff")
@NamedQueries(value = {@NamedQuery(name = "selecWorkTariff", query = "select t from WorkTariff t "),@NamedQuery(name = "getAllTariffs", query = "select t from WorkTariff t ")})
public class WorkTariff implements Serializable {

    private static final long serialVersionUID = -6594469163451494240L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", columnDefinition = "double default 0")
    private double value = 0.0d;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


}
