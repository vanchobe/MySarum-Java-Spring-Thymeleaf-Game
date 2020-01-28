package com.mysarum.controller;

import com.mysarum.implementlogic.ItemLooter;
import com.mysarum.implementlogic.RandomId;
import com.mysarum.model.Dungeon;
import com.mysarum.model.User;
import com.mysarum.model.Weapon;
import com.mysarum.model.Work;
import com.mysarum.service.*;
import com.mysarum.validators.DungeonValidator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Controller
public class DungeonController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private WorkService workService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BackpackService backpackService;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;


    @RequestMapping(value = "/dungeons/mine", method = RequestMethod.GET)
    public ModelAndView dungeonsMine() {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Work.refreshWorkState(workService, userService, user);

        if (user.getDungeonTimestamp() != null) {
            modelAndView.addObject("buyMoreAttacks", 1);
        } else {
            modelAndView.addObject("buyMoreAttacks", 0);

        }

        modelAndView.addObject("monsters", dungeonService.findAll());
        int customId = 3;
        Dungeon.visualizeStats(modelAndView, dungeonService, customId, weaponService, user);

        User.visualizeUserStats(modelAndView, user, weaponService);

        modelAndView.setViewName("dungeons/mine");
        return modelAndView;
    }

    @RequestMapping(value = "/buy-more-attacks", method = RequestMethod.POST)
    public ModelAndView buyMoreAttacks() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        Weapon weapon = weaponService.findWeaponById(user.getWeapon());


        // refresh work state
        Work.refreshWorkState(workService, userService, user);

        int customId = 3;
        Dungeon.visualizeStats(modelAndView, dungeonService, customId, weaponService, user);
        User.visualizeUserStats(modelAndView, user, weaponService);

        int playerGold = user.getGold();

        if (user.getDungeonTimestamp() != null) {
            if (user.getGold() >= 10) {
                user.setDungeonTimestamp(null);
                user.setGold(playerGold - 10);
                userService.saveStats(user);
                modelAndView.addObject("successBuyBattlesMessage", "You add +10 new battles.");
                modelAndView.setViewName("dungeons/mine");
            } else {
                modelAndView.addObject("successBuyBattlesMessage", "You don't have enought gold. Need 10. Try again later!");
                modelAndView.setViewName("dungeons/mine");
            }
        } else {

            modelAndView.addObject("successBuyBattlesMessage", "You can't buy new battles. You have free turns.");
            modelAndView.setViewName("dungeons/mine");
        }

        modelAndView.setViewName("dungeons/mine");

        return modelAndView;
    }

    @RequestMapping(value = "/attack/{id}", method = RequestMethod.POST)
    public ModelAndView attack(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        Weapon weapon = weaponService.findWeaponById(user.getWeapon());

        Random rand = new Random();

        // refresh work state
        Work.refreshWorkState(workService, userService, user);


        // TODo da vidq kak sa atakuva geroq

        int monsterPower = dungeonService.findById(id).getPower();
        int monsterHealth = dungeonService.findById(id).getHealth();
        int goldDrop = dungeonService.findById(id).getGoldDrop();
        int xpDrop = dungeonService.findById(id).getXpDrop();
        int levelRequired = dungeonService.findById(id).getLevelRequired();

        int playerHealth = user.getHealth();
        int playerMana = user.getMana();
        int playerGold = user.getGold();
        int playerExperience = user.getExperience();
        int playerLevel = user.getLevel();

        int randomGoldLoot = rand.nextInt(goldDrop);
        int randomXpLoot = rand.nextInt(xpDrop);


        Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
        User.visualizeUserStats(modelAndView, user, weaponService);

        int weaponBonusExperience = weaponService.findWeaponById(user.getWeapon()).getBonusExperience();


        // refresh attack monsters timestamp
        LocalDateTime currentGameTime = user.getDungeonTimestamp();
        if (user.getDungeonTimestamp() != null) {
            boolean result = (currentGameTime.plusMinutes(60).compareTo(currentGameTime = LocalDateTime.now()) <= -1);
            if (result) {
                user.setDungeonTimestamp(null);
                userService.saveStats(user);
            }

        }
        boolean isValid = false;
        if (DungeonValidator.validatePlayerPower(user.getPower(), weapon.getWeaponPoints(), monsterPower)) {
            isValid = true;
        } else {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You can't attack. Your power is lower than monster");
        }
        if (DungeonValidator.validatePlayerHealth(playerHealth) && isValid) {
            isValid = true;
        } else if (isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You can't attack. Your health is 0");
        }
        if (DungeonValidator.validatePlayerVsMonsterHealth(playerHealth, monsterHealth) && isValid) {
            isValid = true;
        } else if (isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You can't attack. Your health is lower than monster");
        }
        if (DungeonValidator.validatePlayerLevel(playerLevel, levelRequired) && isValid) {
            isValid = true;
        } else if (isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You can't attack. Your level lower than required");
        }

        if (DungeonValidator.validatePlayerMana(playerMana, ((monsterPower * 3) / 4)) && isValid) {
            isValid = true;
        } else if (isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", String.format("You can't attack. Your mana must be: %d", ((monsterPower * 3) / 4)));
        }


        if (isValid) {
            if (!user.isOnWork()) {

                int currentAttackedMonsters = user.getAttackedMonsters();


                LocalDateTime currentTime = user.getDungeonTimestamp();

                if (user.getDungeonTimestamp() == null && currentAttackedMonsters + 1 >= 11) {
                    user.setAttackedMonsters(0);
                    user.setDungeonTimestamp(LocalDateTime.now());
                    Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
                    modelAndView.setViewName("dungeons/mine");
                    modelAndView.addObject("successAttackMessage", "You attacked 10/10 monsters. You must wait 1 hour and then you can attack again!");
                    userService.saveStats(user);
                } else if (user.getDungeonTimestamp() != null && !(currentTime.plusMinutes(60).compareTo(LocalDateTime.now()) <= -1)) {
                    long timeToWait = ChronoUnit.MINUTES.between(LocalDateTime.now(), currentTime.plusMinutes(60));
                    modelAndView.addObject("successAttackMessage", String.format("You attacked 10/10 monsters. You must wait %d minutes and then you can attack again!", timeToWait));
                    Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
                    modelAndView.setViewName("dungeons/mine");
                } else if (user.getDungeonTimestamp() == null && currentAttackedMonsters + 1 <= 10) {
                    user.setAttackedMonsters(currentAttackedMonsters + 1);
                    user.setHealth(playerHealth - monsterHealth);
                    user.setMana(playerMana - ((monsterPower * 3) / 4));
                    user.setGold(randomGoldLoot + playerGold);
                    user.setExperience(playerExperience + randomXpLoot + (weaponBonusExperience / 4));

                    int currentMonstersAttacked = user.getAttackedMonsters();
                    modelAndView.addObject("successAttackMessage", String.format("Attack was successfull! You loose %d health.%n Gained +%d Xp and +%d Gold. Attacked %d / 10",
                            monsterPower,
                            randomXpLoot + (weaponBonusExperience / 4),
                            randomGoldLoot,
                            (currentAttackedMonsters + 1)
                    ));


                    userService.saveStats(user);

                    // Add chance to win random Items Loot when attack


                    // add active weapon bonus on drop (increase chance to win when have good weapon)
                    int weaponBonusDrop = weaponService.findWeaponById(user.getWeapon()).getBonusItemDrop();

                    int randomId = RandomId.generateRandomAttack(weaponBonusDrop);
                    ItemLooter.loot(randomId, modelAndView, itemService, backpackService, user);

                    Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
                    modelAndView.setViewName("dungeons/mine");
                    userService.saveStats(user);
                }

            } else {
                Work.refreshWorkState(workService, userService, user);
                modelAndView.addObject("successAttackMessage", String.format("You can't attack when Work. Come back later"));
                Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
                modelAndView.setViewName("dungeons/mine");

            }
        } else {

            Dungeon.visualizeStats(modelAndView, dungeonService, id, weaponService, user);
            modelAndView.setViewName("dungeons/mine");

        }


        return modelAndView;
    }
}
