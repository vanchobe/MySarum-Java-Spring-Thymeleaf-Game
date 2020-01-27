package com.mysarum.model;

import com.mysarum.service.DungeonService;
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
@Table(name = "MONSTER")
public class Dungeon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MONSTER_ID")
    private int id;
    @Column(name = "MONSTER_NAME")
    private String name;
    @Column(name = "MONSTER_HEALTH")
    private int health;
    @Column(name = "MONSTER_POWER")
    private int power;
    @Column(name = "MONSTER_IMAGE")
    private String image;
    @Column(name = "GOLD_DROP")
    private int goldDrop;
    @Column(name = "XP_DROP")
    private int xpDrop;
    @Column(name = "LEVEL_REQUIRED")
    private int levelRequired;

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }

    public int getXpDrop() {
        return xpDrop;
    }

    public void setXpDrop(int xpDrop) {
        this.xpDrop = xpDrop;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    public void setGoldDrop(int goldDrop) {
        this.goldDrop = goldDrop;
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static void visualizeStats(ModelAndView modelAndView, DungeonService dungeonService, int id, WeaponService weaponService,User user){
        modelAndView.addObject("monsterName", dungeonService.findById(id).getName());
        modelAndView.addObject("monsterHealth", dungeonService.findById(id).getHealth());
        modelAndView.addObject("monsterPower", dungeonService.findById(id).getPower());
        modelAndView.addObject("monsterLevelRequired", dungeonService.findById(id).getLevelRequired());
        modelAndView.addObject("monsterImage", dungeonService.findById(id).getImage());
        modelAndView.addObject("monsters", dungeonService.findAll());

        modelAndView.addObject("userLevel", user.getLevel());

        modelAndView.addObject("playerWeaponImage", weaponService.findWeaponById(user.getWeapon()).getWeaponImage());


    }


}
