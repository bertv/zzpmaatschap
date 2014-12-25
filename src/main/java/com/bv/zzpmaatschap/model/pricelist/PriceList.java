package com.bv.zzpmaatschap.model.pricelist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.bv.zzpmaatschap.model.Item;
import com.bv.zzpmaatschap.model.TenantEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;


@Entity
@XmlRootElement
@Table(name="pricelist",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@NamedQueries(value = { @NamedQuery(name = "selectPriceList", query = "select p from PriceList p where p.name=:name"),@NamedQuery(name = "selectPriceLists", query = "select p from PriceList p") })
public class PriceList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2685178170482451L;

	/** Default value included to remove warning. Remove or modify at will. **/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25, message = "Naam moet tussen 1 en 25 tekens zijn.")
	private String name;

    @JsonIgnore
    @XmlTransient
	@OneToMany(mappedBy = "priceList", fetch = FetchType.LAZY, orphanRemoval = true,cascade = CascadeType.REMOVE)
	private Set<PriceListItem> items = new HashSet<PriceListItem>();


    private Long total;

    private Long failed;


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
	public Set<PriceListItem> getItems() {
		return items;
	}

	public void setItems(Set<PriceListItem> items) {
		this.items = items;
	}

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getFailed() {
        return failed;
    }

    public void setFailed(Long failed) {
        this.failed = failed;
    }
}
