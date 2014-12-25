package com.bv.zzpmaatschap.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name="quantityunit")
@NamedQueries(value = { @NamedQuery(name = "selectQuantityUnit", query = "select q from QuantityUnit q") })
public class QuantityUnit implements Serializable {
    private static final long serialVersionUID = 423235420994383l;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 0, max = 50, message = "Naam mag maximaal 50 tekens zijn.")
    private String name;

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
}
