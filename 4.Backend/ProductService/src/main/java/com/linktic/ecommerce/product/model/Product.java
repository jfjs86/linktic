package com.linktic.ecommerce.product.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ec_product")
public class Product {

    @Id
    @Column(name = "ecp_id",unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ecp_name",nullable = false)
    private String name;
    @Column(name = "ecp_description",nullable = false)
    private String description;
    @Column(name = "ecp_image_path",nullable = false)
    private String imagePath;
    @Column(name = "ecp_price",nullable = false)
    private BigDecimal price;
    @Column(name = "ecp_available",nullable = false)
    private int available;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
