package com.mysarum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_ID")
    private int id;
    @Column(name = "ITEM_NAME")
    private String name;
    @Column(name = "ITEM_POINTS")
    private int itemPoints;
    @Column(name = "ITEM_IMAGE")
    private String itemImage;
    @Column(name = "ITEM_PRICE")
    private int price;
    @Column(name = "ITEM_SELL_PRICE")
    private int sellPrice;
    @Column(name = "ITEM_EXPERIENCE_EARN")
    private int experienceEarn;

    public int getExperienceEarn() {
        return experienceEarn;
    }

    public void setExperienceEarn(int experienceEarn) {
        this.experienceEarn = experienceEarn;
    }

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

    public int getItemPoints() {
        return itemPoints;
    }

    public void setItemPoints(int itemPoints) {
        this.itemPoints = itemPoints;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
}