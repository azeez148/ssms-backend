package com.ad.ssms.product;

import java.util.Map;

import com.ad.ssms.category.Category;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


     // Define the sizeMap as an ElementCollection
     @ElementCollection
     @MapKeyColumn(name = "size")  // This defines the name of the column that will hold the map keys (sizes)
     @Column(name = "quantity")    // This defines the name of the column that will hold the map values (quantities)
     private Map<String, Integer> sizeMap;

     private int unitPrice;
     private int sellingPrice;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, Integer> getSizeMap() {
        return sizeMap;
    }

    public void setSizeMap(Map<String, Integer> sizeMap) {
        this.sizeMap = sizeMap;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }
    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
