package com.mysarum.controller;

import com.mysarum.model.Food;
import com.mysarum.model.User;
import com.mysarum.service.FoodService;
import com.mysarum.service.UserService;
import com.mysarum.service.WeaponService;
import com.mysarum.validators.FoodShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class FoodShopController {


    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private FoodService foodService;


    @RequestMapping(value = "/shop/food-shop", method = RequestMethod.GET)
    public ModelAndView foodShop() {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


//        modelAndView.addObject("monsterName", monsterService.findById(1).getName());
//        modelAndView.addObject("monsterHealth", monsterService.findById(1).getHealth());
//        modelAndView.addObject("monsterPower", monsterService.findById(1).getPower());
//        modelAndView.addObject("monsterImage", monsterService.findById(1).getImage());


        User.visualizeUserStats(modelAndView, user, weaponService);

        Food.foodVisualize(modelAndView, foodService);

        modelAndView.setViewName("shop/food-shop");
        return modelAndView;
    }


    @RequestMapping(value = "/buy-food/{id}", method = RequestMethod.POST)
    public ModelAndView buyFood(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


//        Weapon weapon = weaponService.findWeaponById(user.getWeapon());


        int hpRestore = foodService.findById(id).getHpRestore();
        int manaRestore = foodService.findById(id).getManaRestore();
        int foodPrice = foodService.findById(id).getPrice();
        int levelRequired = foodService.findById(id).getRequiredLevel();

        int playerHealth = user.getHealth();
        int playerMana = user.getMana();
        int playerGold = user.getGold();
        int playerLevel = user.getLevel();

        boolean isValid = false;
        if (FoodShopValidator.validatePlayerLevel(playerLevel, levelRequired)) {
            isValid = true;

        } else {
            isValid = false;
            modelAndView.addObject("successAttackMessage", String.format("Level is low. You need %d level to eat this!", levelRequired));

        }
        if (FoodShopValidator.validatePlayerGold(playerGold, foodPrice) && isValid) {
            isValid = true;

        } else if (isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", String.format("You don't have enought gold. You need %d gold.", foodPrice));

        }


        if (isValid) {

            boolean isSuccess = false;


            if (hpRestore + playerHealth <= 100) {
                user.setHealth(playerHealth + hpRestore);
                isSuccess = true;
            } else if (playerHealth != 100) {
                user.setHealth(100);
                isSuccess = true;

            }
            if (manaRestore + playerMana <= 100) {
                user.setMana(playerMana + manaRestore);
                isSuccess = true;
            } else if (playerMana != 100) {
                user.setMana(100);
                isSuccess = true;

            }
            if (isSuccess && isValid) {
                user.setGold(playerGold - foodPrice);
                modelAndView.addObject("successAttackMessage", String.format("You eat food successfully!"));
            } else if(!isSuccess) {
                modelAndView.addObject("successAttackMessage", String.format("You have full xp and mana!"));

            }

            userService.saveStats(user);


            User.visualizeUserStats(modelAndView, user, weaponService);

            Food.foodVisualize(modelAndView, foodService);

            modelAndView.setViewName("shop/food-shop");
        } else {


            User.visualizeUserStats(modelAndView, user, weaponService);

            Food.foodVisualize(modelAndView, foodService);


//            modelAndView.addObject("successAttackMessage", String.format("You don't meet the requirements to buy this food! Check level requirement, gold or you have full health/mana."));
            modelAndView.setViewName("shop/food-shop");

        }


        return modelAndView;
    }

}
