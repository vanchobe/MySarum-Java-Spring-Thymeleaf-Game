package com.mysarum.controller;

import com.mysarum.model.User;
import com.mysarum.service.FoodService;
import com.mysarum.service.UserService;
import com.mysarum.service.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TopPlayersController {


    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private FoodService foodService;


    @RequestMapping(value = "/top-players/page/{page}", method = RequestMethod.GET)
    public ModelAndView topPlayerspageByPage(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("top-players-paging");

        int size = 5;


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        User.visualizeUserStats(modelAndView, user, weaponService);


        TopPlayersController.visualizePlayers(userService, modelAndView, page, size);


        modelAndView.setViewName("top-players");
        return modelAndView;
    }

    public static void visualizePlayers(UserService userService, ModelAndView modelAndView, int page, int size) {

        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("level").descending());


        Page<User> playersList = userService.getPaginatedUsers(pageable);


        int totalPages = playersList.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        modelAndView.addObject("activeArticleList", true);
        modelAndView.addObject("players", playersList.getContent());

//        modelAndView.addObject("players", playersList);


        if (page == 1) {
            modelAndView.addObject("counter", 1);
        } else {

            modelAndView.addObject("counter", ((size * page) - size) + 1);
        }


    }

}