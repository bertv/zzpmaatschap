package com.bv.zzpmaatschap.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name="materialitem")
@NamedQueries(value = { @NamedQuery(name = "selectMaterialItem", query = "select m from MaterialItem m ") })
public class MaterialItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6594469163451494240L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;


	private Double quantity;

    @ManyToOne
	private QuantityUnit type;

	private Double price;

    private String comment;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	


	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuantityUnit getType() {
        return type;
    }

    public void setType(QuantityUnit type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void copy(MaterialItem newMaterialItem) {
        newMaterialItem.setName(this.name);
        newMaterialItem.setComment(this.comment);
        newMaterialItem.setPrice(this.price);
        newMaterialItem.setType(this.type);
        newMaterialItem.setQuantity(this.quantity);
    }
}
