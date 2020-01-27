package com.mysarum.model;

import com.mysarum.service.WeaponService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private int id;
    @Column(name = "EMAIL")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @Column(name = "PASSWORD")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;
    @Column(name = "NAME")
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "LAST_NAME")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;
    @Column(name = "ACTIVE")
    private int active;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles;


    @Column(name = "HEALTH")
    private int health;

    @Column(name = "MANA")
    private int mana;

    @Column(name = "POWER")
    private int power;

    @Column(name = "EXPERIENCE")
    private int experience;

    @Column(name = "LEVEL")
    private int level;

    @Column(name = "GOLD")
    private int gold;

    @Column(name = "WEAPON")
    private int weapon;

    @Column(name = "WORK_STATE")
    private boolean onWork;

    @Column(name = "ATTACKED_MONSTERS")
    private int attackedMonsters;

    @Column(name = "`dungeon_timestamp`")
    private LocalDateTime dungeonTimestamp;

    public int getAttackedMonsters() {
        return attackedMonsters;
    }

    public void setAttackedMonsters(int attackedMonsters) {
        this.attackedMonsters = attackedMonsters;
    }

    public LocalDateTime getDungeonTimestamp() {
        return dungeonTimestamp;
    }

    public void setDungeonTimestamp(LocalDateTime dungeonTimestamp) {
        this.dungeonTimestamp = dungeonTimestamp;
    }

    public boolean isOnWork() {
        return onWork;
    }

    public void setOnWork(boolean onWork) {
        this.onWork = onWork;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWeapon() {
        return weapon;
    }

    public void setWeapon(int weapon) {
        this.weapon = weapon;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static void visualizeUserStats(ModelAndView modelAndView, User user, WeaponService weaponService) {
        modelAndView.addObject("userWeapon", String.format("Weapon: %s Power: %d ", weaponService.findWeaponById(user.getWeapon()).getName(), weaponService.findWeaponById(user.getWeapon()).getWeaponPoints()));
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage", "This Page is available to Users with User Role");

        modelAndView.addObject("playerName", user.getName());
        modelAndView.addObject("playerHealth", user.getHealth());

        modelAndView.addObject("weaponImage", weaponService.findWeaponById(user.getWeapon()).getWeaponImage());

        modelAndView.addObject("userHealth", user.getHealth());
        modelAndView.addObject("userMana", user.getMana());
        modelAndView.addObject("userPower", (user.getPower() + weaponService.findWeaponById(user.getWeapon()).getWeaponPoints()));
        modelAndView.addObject("userBonusPower", String.format("(Bonus: %s)", weaponService.findWeaponById(user.getWeapon()).getName()));
        modelAndView.addObject("userExperience", user.getExperience());
        modelAndView.addObject("userLevel", user.getLevel());
        modelAndView.addObject("userGold", user.getGold());

    }
}