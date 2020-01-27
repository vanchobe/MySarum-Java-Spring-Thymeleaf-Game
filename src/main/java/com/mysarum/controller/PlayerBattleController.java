package com.mysarum.controller;

import com.mysarum.model.User;
import com.mysarum.service.UserService;
import com.mysarum.service.WeaponService;
import com.mysarum.validators.PlayerBattleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class PlayerBattleController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;


    @RequestMapping(value = "/player-battle", method = RequestMethod.GET)
    public ModelAndView playerBattlePage() {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        User.visualizeUserStats(modelAndView, user, weaponService);


//        List<User> sortUsers = new ArrayList<>();
//        sortUsers.addAll(userService.findAll());
//
//        sortUsers = sortUsers.stream().sorted((f, s) -> s.getPower() - f.getPower()).collect(Collectors.toList());
//
        int size = 5;

        // to visualize players
        PlayerBattleController.visualizePlayers(userService, modelAndView, size, 1);

        modelAndView.setViewName("player-battle");
        return modelAndView;
    }


    @RequestMapping(value = "/player-battle/{page}", method = RequestMethod.GET)
    public ModelAndView playerBattlePaging(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        User.visualizeUserStats(modelAndView, user, weaponService);


//        List<User> sortUsers = new ArrayList<>();
//        sortUsers.addAll(userService.findAll());
//
//        sortUsers = sortUsers.stream().sorted((f, s) -> s.getPower() - f.getPower()).collect(Collectors.toList());
//
        int size = 5;

        PlayerBattleController.visualizePlayers(userService, modelAndView, size, page);


        modelAndView.setViewName("player-battle");
        return modelAndView;
    }

    @RequestMapping(value = "/player-battle/{id}", method = RequestMethod.POST)
    public ModelAndView playerBattle(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentPlayer = userService.findUserByEmail(auth.getName());

        User attackedPlayer = userService.findById(id);


        int attackerPlayerHealth = currentPlayer.getHealth();
        int attackerPlayerMana = currentPlayer.getMana();
        int attackerPlayerGold = currentPlayer.getGold();
        int attackerPlayerLevel = currentPlayer.getLevel();
        int attackerPlayerPower = currentPlayer.getPower();

        int defenderPlayerHealth = attackedPlayer.getHealth();
        int defenderPlayerMana = attackedPlayer.getMana();
        int defenderPlayerGold = attackedPlayer.getGold();
        int defenderPlayerLevel = attackedPlayer.getLevel();
        int defenderPlayerPower = attackedPlayer.getPower();

        boolean canAttack = false;
        if (PlayerBattleValidator.validatePlayerPower(attackerPlayerPower, defenderPlayerPower)) {
            canAttack = true;

        } else {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", String.format("You can't attack! Your player power is lower than %s", attackedPlayer.getName()));

        }
        if (PlayerBattleValidator.validateAttackerMana(attackerPlayerMana) && canAttack) {

            canAttack = true;
        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Your mana is low");
        }
        if (PlayerBattleValidator.validateAttackerHealth(attackerPlayerHealth) && canAttack) {
            canAttack = true;

        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Your health is low");
        }
        if (PlayerBattleValidator.validatePlayerLevel(attackerPlayerLevel, defenderPlayerLevel) && canAttack) {
            canAttack = true;

        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Your level is low.");
        }
        if (PlayerBattleValidator.validateDefenderHealth(defenderPlayerHealth) && canAttack) {
            canAttack = true;
        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Defender health is low.");
        }
        if (PlayerBattleValidator.validateDefenderMana(defenderPlayerMana) && canAttack) {
            canAttack = true;

        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Defender mana is low.");
        }
        if (PlayerBattleValidator.validateDefenderGold(defenderPlayerGold) && canAttack) {
            canAttack = true;

        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack! Defender's gold is 0.");
        }

        if (PlayerBattleValidator.validatePlayersIds(currentPlayer.getId(), attackedPlayer.getId()) && canAttack) {
            canAttack = true;
        } else if (canAttack) {
            canAttack = false;
            modelAndView.addObject("successAttackMessage", "You can't attack yourself!");

        }
        if (canAttack) {

            currentPlayer.setHealth(attackerPlayerHealth - 25);
            currentPlayer.setMana(attackerPlayerMana - 50);
            currentPlayer.setGold(attackerPlayerGold + 1);

            attackedPlayer.setHealth(defenderPlayerHealth - 30);
            attackedPlayer.setMana(defenderPlayerMana - 10);
            attackedPlayer.setGold(defenderPlayerGold - 1);

            userService.saveStats(currentPlayer);
            userService.saveStats(attackedPlayer);

            int size = 5;

            PlayerBattleController.visualizePlayers(userService, modelAndView, size, 1);


            modelAndView.addObject("successAttackMessage", String.format("You attack player successfully"));
            modelAndView.setViewName("player-battle");
        } else {


            int size = 5;
            PlayerBattleController.visualizePlayers(userService, modelAndView, size, 1);


            modelAndView.setViewName("player-battle");

        }
        User.visualizeUserStats(modelAndView, currentPlayer, weaponService);


//        modelAndView.addObject("successAttackMessage", String.format("You don't meet the requirements to buy this food! Check level requirement, gold or you have full health/mana."));
        modelAndView.setViewName("player-battle");


        return modelAndView;
    }

    public static void visualizePlayers(UserService userService, ModelAndView modelAndView, int size, int page) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userService.getPaginatedUsers(pageable);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        modelAndView.addObject("players", userPage.getContent());


        if (page == 1) {
            modelAndView.addObject("counter", 1);
        } else {

            modelAndView.addObject("counter", ((size * page) - size) + 1);
        }


    }
}
