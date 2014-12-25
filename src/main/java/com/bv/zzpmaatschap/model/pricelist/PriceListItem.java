package com.bv.zzpmaatschap.model.pricelist;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.bv.zzpmaatschap.model.TenantEntity;


@Entity
@XmlRootElement
@Table(name="pricelistitem",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries(value = { @NamedQuery(name = "selectPriceListItem", query = "select p from PriceListItem p where p.name LIKE :name") ,@NamedQuery(name = "getPriceList", query = "select count(p) from PriceListItem p where p.priceList.id= :priceListId")})
public class PriceListItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 268514570482451L;

	/** Default value included to remove warning. Remove or modify at will. **/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 255, message = "Naam moet tussen 1 en 255 tekens zijn.")
	private String name;

	@ManyToOne
	private PriceList priceList;
	
	private String itemNumber;
	
	private String unit;
	
	private Integer amount;
	
	private BigDecimal price;
	
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public String toString() {
		return "PriceListItem [id=" + id + ", name=" + name + ", priceList="
				+ priceList + ", itemNumber=" + itemNumber + ", unit=" + unit
				+ ", amount=" + amount + ", price=" + price + "]";
	}

    public void setPriceList(PriceList priceList) {
        this.priceList = priceList;
    }
}
