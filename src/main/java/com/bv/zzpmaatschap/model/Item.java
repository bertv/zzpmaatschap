package com.bv.zzpmaatschap.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@XmlRootElement
@Table(name="item")
@NamedQueries(value = {@NamedQuery(name = "selectItem", query = "select i from Item i join fetch i.offer where i.offer.id=:offer"), @NamedQuery(name = "getOrderedItems", query = "select i from Item i join fetch i.offer where i.offer.id=:offer order by i.popularity desc,i.orderNumber asc,i.id asc"), @NamedQuery(name = "getDescriptions", query = "select i from Item i join fetch i.offer where i.offer.id=:offer and i.description LIKE :query order by i.orderNumber,i.id"), @NamedQuery(name = "getItemByDescription", query = "select i from Item i join fetch i.offer where i.offer.id=:offer and i.description = :description"), @NamedQuery(name = "getAllItems", query = "select i from Item i where i.offer is not null order by i.popularity desc,i.created desc,i.orderNumber asc,i.id asc")})
public class Item implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 163543005L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(min = 0, max = 200, message = "Omschrijving mag maximaal 200 tekens zijn.")
    private String description;


    @ManyToOne
    private Offer offer;


    @ManyToOne
    private Category category;

    @ManyToOne
    private ItemType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<MaterialItem> materials = new HashSet<MaterialItem>();


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<WorkItem> works = new HashSet<WorkItem>();

    private int orderNumber;

    private String comment;

    private int size;

    private int popularity;

    private Date created;

    @Transient
    private int terugrekenwaarde;

    public Set<MaterialItem> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<MaterialItem> materials) {
        this.materials = materials;
    }


    public Set<WorkItem> getWorks() {
        return works;
    }

    public void setWorks(Set<WorkItem> works) {
        this.works = works;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

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

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int order) {
        this.orderNumber = order;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void copy(Item newItem) {
        newItem.setDescription(this.description);
        newItem.setCategory(this.category);
        newItem.setOrderNumber(this.orderNumber);
        newItem.setType(this.type);
        newItem.setPopularity(this.popularity);
        newItem.setComment(this.comment);
        newItem.setSize(this.size);
        newItem.setCreated(new Date());
        for (MaterialItem materialItem : this.materials) {
            MaterialItem newMaterialItem = new MaterialItem();
            materialItem.copy(newMaterialItem);
            newItem.addMaterialItem(newMaterialItem);
        }
        for (WorkItem workItem : this.works) {
            WorkItem newWorkItem = new WorkItem();
            workItem.copy(newWorkItem);
            newItem.addWorkItem(newWorkItem);
        }
//        this.copyTenant(newItem);
    }

    private void addWorkItem(WorkItem newWorkItem) {
        this.works.add(newWorkItem);
    }

    private void addMaterialItem(MaterialItem newMaterialItem) {
        this.materials.add(newMaterialItem);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTerugrekenwaarde() {
        return terugrekenwaarde;
    }

    public void setTerugrekenwaarde(int terugrekenwaarde) {
        this.terugrekenwaarde = terugrekenwaarde;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Item item = (Item) o;
//
//        if (orderNumber != item.orderNumber) return false;
//        if (!description.equals(item.description)) return false;
//        if (!id.equals(item.id)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id.hashCode();
//        result = 31 * result + description.hashCode();
//        result = 31 * result + orderNumber;
//        return result;
//    }

}
