package com.mysarum.controller;

import com.mysarum.model.User;
import com.mysarum.model.Weapon;
import com.mysarum.service.UserService;
import com.mysarum.service.WeaponService;
import com.mysarum.validators.WeaponShopValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WeaponShopController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @RequestMapping(value = "/shop/weapon-shop", method = RequestMethod.GET)
    public ModelAndView weaponShop() {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        Weapon.visualizeWeapon(modelAndView, weaponService);


        User.visualizeUserStats(modelAndView, user, weaponService);


        modelAndView.setViewName("shop/weapon-shop");
        return modelAndView;
    }

    @RequestMapping(value = "/buy-weapon/{id}", method = RequestMethod.POST)
    public ModelAndView buyWeapon(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        Weapon weapon = weaponService.findWeaponById(user.getWeapon());


        int weaponPrice = weaponService.findWeaponById(id).getPrice();
        int weaponPower = weaponService.findWeaponById(id).getWeaponPoints();
        int levelRequired = weaponService.findWeaponById(id).getLevelRequired();


        int playerGold = user.getGold();
        int playerLevel = user.getLevel();
        int playerPower = user.getPower();

        int playerCurrentWeaponId = user.getWeapon();

        int playerCurrentWeaponPower = weaponService.findWeaponById(playerCurrentWeaponId).getWeaponPoints();

        boolean isValid = false;
        if (WeaponShopValidator.validatePlayerLevel(playerLevel, levelRequired)) {
            isValid = true;
        } else  {
            isValid = false;
            modelAndView.addObject("successAttackMessage", String.format("Your level is low than required. You need %d level.",levelRequired));
        }
        if (WeaponShopValidator.validatePlayerGold(playerGold, weaponPrice) && isValid) {
            isValid = true;
        } else if(isValid){
            isValid = false;
            modelAndView.addObject("successAttackMessage", String.format("Your don't have enought gold! You need %d gold.",weaponPrice));
        }
        if (WeaponShopValidator.validateHaveSameWeapon(user.getWeapon(), id) && isValid) {
            isValid = true;
        } else if(isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You have this weapon!");
        }
        if (WeaponShopValidator.validateHaveBetterWeapon(user.getWeapon(), id) && isValid) {
            isValid = true;
        } else if(isValid) {
            isValid = false;
            modelAndView.addObject("successAttackMessage", "You have better weapon!");
        }

        if (isValid) {
            user.setGold(playerGold - weaponPrice);
            user.setPower((playerPower - playerCurrentWeaponPower) + weaponPower);
            user.setWeapon(id);

            modelAndView.addObject("successAttackMessage", String.format("You bought this weapon successfully!"));


            userService.saveStats(user);


            User.visualizeUserStats(modelAndView, user, weaponService);

            Weapon.visualizeWeapon(modelAndView, weaponService);

            modelAndView.setViewName("shop/weapon-shop");
        } else {


            User.visualizeUserStats(modelAndView, user, weaponService);

            Weapon.visualizeWeapon(modelAndView, weaponService);

//            modelAndView.addObject("successAttackMessage", String.format("You don't meet the requirements to buy this weapon! Check level requirement, gold or you have powerfull weapon."));
            modelAndView.setViewName("shop/weapon-shop");

        }


        return modelAndView;
    }
}
