package com.mysarum.controller;

import com.mysarum.implementlogic.ItemLooter;
import com.mysarum.implementlogic.RandomId;
import com.mysarum.model.User;
import com.mysarum.model.Work;
import com.mysarum.service.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
public class WorkController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private FoodService foodService;

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


    @RequestMapping(value = "/work", method = RequestMethod.GET)
    public ModelAndView work() {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        Work.refreshWorkState(workService, userService, user);


        modelAndView.addObject("foods", foodService.findAll());


        User.visualizeUserStats(modelAndView, user, weaponService);

        modelAndView.setViewName("work");
        return modelAndView;
    }


    @RequestMapping(value = "/work", method = RequestMethod.POST)
    public ModelAndView doWork(@Valid Work work, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


//        Weapon weapon = weaponService.findWeaponById(user.getWeapon());


        if (workService.findByPlayerId(user.getId()) == null) {

            work.setPlayerId(user.getId());
            work.setTimestamp(createDateTime = LocalDateTime.now());


            workService.saveWork(work);

            user.setOnWork(true);

            int weaponBonusExperience = weaponService.findWeaponById(user.getWeapon()).getBonusExperience();

            int currentGold = user.getGold();
            int currentXp = user.getExperience();
            user.setGold(currentGold + user.getLevel());
            user.setExperience(currentXp + (user.getLevel() * 4) + weaponBonusExperience);
            userService.saveStats(user);
            //TODO add random item earned


            int weaponBonusDrop = weaponService.findWeaponById(user.getWeapon()).getBonusItemDrop();

            int randomId = RandomId.generateRandomWorkId(weaponBonusDrop);
            ItemLooter.loot(randomId, modelAndView, itemService, backpackService, user);


            modelAndView.addObject("successWorkMessage", "Worked success");

            User.visualizeUserStats(modelAndView, user, weaponService);
            modelAndView.setViewName("work");
        } else {


            LocalDateTime currentTime = workService.findByPlayerId(user.getId()).getTimestamp();
            boolean result = (currentTime.plusMinutes(30).compareTo(LocalDateTime.now()) <= -1);
            if (result) {
                workService.deleteWork(user.getId());
                user.setOnWork(false);
                userService.saveStats(user);
            } else {
                long timeToWait = ChronoUnit.MINUTES.between(LocalDateTime.now(), currentTime.plusMinutes(30));

                modelAndView.addObject("errorWorkMessage", String.format("You are currently working. You will finish the work after %d minutes", timeToWait));

            }
            User.visualizeUserStats(modelAndView, user, weaponService);


            modelAndView.setViewName("work");

        }


        return modelAndView;
    }


}
