package com.mysarum.model;

import com.mysarum.service.FoodService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FOOD")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FOOD_ID")
    private int id;
    @Column(name = "FOOD_NAME")
    private String name;
    @Column(name = "FOOD_PRICE")
    private int price;
    @Column(name = "FOOD_HP_RESTORE")
    private int hpRestore;
    @Column(name = "FOOD_MANA_RESTORE")
    private int manaRestore;
    @Column(name = "FOOD_REQUIRED_LEVEL")
    private int requiredLevel;
    @Column(name = "FOOD_IMAGE")
    private String image;

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHpRestore() {
        return hpRestore;
    }

    public void setHpRestore(int hpRestore) {
        this.hpRestore = hpRestore;
    }

    public int getManaRestore() {
        return manaRestore;
    }

    public void setManaRestore(int manaRestore) {
        this.manaRestore = manaRestore;
    }

    public static void foodVisualize(ModelAndView modelAndView, FoodService foodService) {
        modelAndView.addObject("foods", foodService.findAll());
    }
}
