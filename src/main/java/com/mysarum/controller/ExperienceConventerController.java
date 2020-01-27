package com.mysarum.controller;

import com.mysarum.model.User;
import com.mysarum.service.FoodService;
import com.mysarum.service.DungeonService;
import com.mysarum.service.UserService;
import com.mysarum.service.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExperienceConventerController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private FoodService foodService;


    @RequestMapping(value = "/convert-experience/", method = RequestMethod.POST)
    public ModelAndView convertXp() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        int playerExperience = user.getExperience();
        int playerLevel = user.getLevel();

        int xpRequired = 0;

        int count = 1;
        for (int i = 4; i < 1000; i += 4) {
            if (playerLevel == count) {
                xpRequired = i;
            }
            count++;
        }

        xpRequired *= 10;


        if (playerExperience >= xpRequired) {

            user.setExperience(playerExperience - xpRequired);
            user.setLevel(playerLevel + 1);


            userService.saveStats(user);

            modelAndView.addObject("successConvertMessage", String.format("Level up! +1 level !"));

            User.visualizeUserStats(modelAndView, user, weaponService);
            modelAndView.setViewName("user/userHome");
        } else {


            User.visualizeUserStats(modelAndView, user, weaponService);

            modelAndView.addObject("successConvertMessage", String.format("You don't meet the requirements to convert xp. Please come later."));
            modelAndView.setViewName("user/userHome");

        }


        return modelAndView;
    }

}
