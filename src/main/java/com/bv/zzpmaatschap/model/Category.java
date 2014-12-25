package com.bv.zzpmaatschap.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Entity
@XmlRootElement
@Table(name="category",uniqueConstraints = @UniqueConstraint(columnNames = "description"))
@NamedQueries(value = { @NamedQuery(name = "selectCategories", query = "select c from Category c"), @NamedQuery(name = "findCategory", query = "select c from Category c where c.description=:description")  })
public class Category implements Serializable {
	/** Default value included to remove warning. Remove or modify at will. **/
	private static final long serialVersionUID = 16375635L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25, message = "Omschrijving moet tussen 1 en 25 tekens zijn.")
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
