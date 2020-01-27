package com.mysarum.model;

import com.mysarum.service.WeaponService;
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
@Table(name = "WEAPON")
public class Weapon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WEAPON_ID")
    private int id;
    @Column(name = "WEAPON_NAME")
    private String name;
    @Column(name = "WEAPON_POINTS")
    private int weaponPoints;
    @Column(name = "WEAPON_IMAGE")
    private String weaponImage;
    @Column(name = "WEAPON_PRICE")
    private int price;
    @Column(name = "WEAPON_LEVEL_REQUIRED")
    private int levelRequired;
    @Column(name = "WEAPON_EXPERIENCE_BONUS")
    private int bonusExperience;
    @Column(name = "WEAPON_ITEM_DROP_BONUS")
    private int bonusItemDrop;

    public int getBonusExperience() {
        return bonusExperience;
    }

    public void setBonusExperience(int bonusExperience) {
        this.bonusExperience = bonusExperience;
    }

    public int getBonusItemDrop() {
        return bonusItemDrop;
    }

    public void setBonusItemDrop(int bonusItemDrop) {
        this.bonusItemDrop = bonusItemDrop;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public String getWeaponImage() {
        return weaponImage;
    }


    public void setWeaponImage(String weaponImage) {
        this.weaponImage = weaponImage;
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

    public int getWeaponPoints() {
        return weaponPoints;
    }

    public void setWeaponPoints(int weaponPoints) {
        this.weaponPoints = weaponPoints;
    }

    public static void visualizeWeapon(ModelAndView modelAndView, WeaponService weaponService) {
        modelAndView.addObject("weapons", weaponService.findAll());
    }
}
