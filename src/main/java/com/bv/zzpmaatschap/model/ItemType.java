package com.bv.zzpmaatschap.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name="itemtype")
@NamedQueries(value = { @NamedQuery(name = "selectItemType", query = "select i from ItemType i") })
public class ItemType implements Serializable{
    private static final long serialVersionUID = 42323594383l;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 0, max = 200, message = "Omschrijving mag maximaal 200 tekens zijn.")
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
