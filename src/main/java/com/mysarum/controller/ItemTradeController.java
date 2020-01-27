package com.mysarum.controller;

import com.mysarum.model.BackPack;
import com.mysarum.model.Item;
import com.mysarum.model.Trade;
import com.mysarum.model.User;
import com.mysarum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ItemTradeController {

    @Autowired
    private UserService userService;

    @Autowired
    private BackpackService backpackService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private WeaponService weaponService;


    @RequestMapping(value = "/user/itemtrade/", method = RequestMethod.GET)
    public ModelAndView itemMarketMain() {
        ModelAndView modelAndView = new ModelAndView("item-list-paging");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        int page = 1;


        // visualize trade items

        Trade.visualizeTradeItemsPaging(page, tradeService, modelAndView, user);


//        Trade.visualizeTradeItems(modelAndView, tradeService,tradePage);

        User.visualizeUserStats(modelAndView, user, weaponService);


        modelAndView.setViewName("user/itemtrade");

        return modelAndView;
    }


    @RequestMapping(value = "/user/itemtrade/page/{page}", method = RequestMethod.GET)
    public ModelAndView itemMarket(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("item-list-paging");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

//        PageRequest pageable = PageRequest.of(page - 1, 4, Sort.by("id").descending());
//
//        Page<Trade> tradePage = tradeService.getPaginatedItems(pageable);
//
//        int totalPages = tradePage.getTotalPages();
//
//        if(totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
//            modelAndView.addObject("pageNumbers", pageNumbers);
//        }

//        modelAndView.addObject("articleList", tradePage.getContent());

//
//        modelAndView.addObject("buyerId", user.getId());


//        Trade.visualizeTradeItems(modelAndView, tradeService,tradePage);


        // visualize trade items

        Trade.visualizeTradeItemsPaging(page, tradeService, modelAndView, user);


        User.visualizeUserStats(modelAndView, user, weaponService);


        modelAndView.setViewName("user/itemtrade");

        return modelAndView;
    }


    @RequestMapping(value = "/user/itemtrade-buy/{id}", method = RequestMethod.POST)
    public ModelAndView tradeItem(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        int playerId = user.getId();
        int playerGold = user.getGold();

        Trade deal = tradeService.findDealById(id);

        List<BackPack> backpack = backpackService.findByPid(playerId);

        BackPack myBackpack = new BackPack();


        int itemId = deal.getItemId();
        int sellerId = deal.getSellerId();
        int dealGoldEarn = deal.getPrice();

        User seller = new User();

        for (User user1 : userService.findAll()) {
            if (user1.getId() == sellerId) {
                seller = user1;
                break;
            }
        }
        boolean haveItem = false;
        for (BackPack currentBackpack : backpack) {
            if (currentBackpack.getItemId() == itemId) {
                haveItem = true;
                myBackpack = currentBackpack;
                break;
            }
        }
        int currentQuantity = myBackpack.getQuantity();

        if (deal.getPrice() <= playerGold && user.getId() != sellerId) {
            if (haveItem) {
                if (currentQuantity >= 1) {
                    myBackpack.setQuantity(currentQuantity + 1);
                    backpackService.saveBackpack(myBackpack);

                    user.setGold(playerGold - deal.getPrice());

                    userService.saveStats(user);

                    Trade.visualizeTradeItems(modelAndView, tradeService);

                    // to iter over backpackService
                    List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

                    Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);


                    // here to show backpack

                    int size = 3;

                    BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQuantity);


                    modelAndView.setViewName("user/backpack");
                    modelAndView.addObject("successBuyMessage", String.format("You buy item successfully"));
                    modelAndView.addObject("buyerId", user.getId());
                }

            } else {
                myBackpack.setPlayerId(user.getId());
                myBackpack.setQuantity(1);
                myBackpack.setItemId(itemId);
                backpackService.saveBackpack(myBackpack);

                user.setGold(playerGold - deal.getPrice());

                userService.saveStats(user);



                // to iter over backpackService
                List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

                Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);


                // here to show backpack

                int size = 3;

                BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQuantity);


                Trade.visualizeTradeItems(modelAndView, tradeService);
                modelAndView.addObject("successBuyMessage", String.format("You buy item successfully"));
                modelAndView.addObject("buyerId", user.getId());
                modelAndView.setViewName("user/backpack");

            }
            int currentSellerGold = seller.getGold();

            seller.setGold(currentSellerGold + dealGoldEarn);

            userService.saveStats(seller);


            tradeService.deleteDeal(id);


        } else {

            int page = 1;


            // visualize trade items

            Trade.visualizeTradeItemsPaging(page, tradeService, modelAndView, user);


//            // to iter over backpackService
//            List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);
//
//            Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);
//
//
//            // here to show backpack
//
//            int size = 3;
//
//            BackPack.showBackpackItemsPaging(size,1,backpackService,sortedList,modelAndView,user,itemQuantity);


            modelAndView.addObject("successBuyMessage", String.format("You don't have enought gold to buy item! or You can't buy your items."));
            modelAndView.setViewName("user/itemtrade");
        }

        Trade.visualizeTradeItems(modelAndView, tradeService);

        User.visualizeUserStats(modelAndView, user, weaponService);

//        modelAndView.setViewName("user/backpack");
        return modelAndView;
    }


    @RequestMapping(value = "/user/itemtrade-return/{id}", method = RequestMethod.POST)
    public ModelAndView returnTradeItem(@PathVariable("id") int id, @ModelAttribute("tradeInfo") Trade tradeInfo) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        modelAndView.addObject("buyerName", user.getName());

        int playerId = user.getId();
        int playerGold = user.getGold();

        Trade deal = tradeService.findDealById(id);

        List<BackPack> backpack = backpackService.findByPid(playerId);

        BackPack myBackpack = new BackPack();

        int itemId = deal.getItemId();
        int sellerId = deal.getSellerId();
        int dealGoldEarn = deal.getPrice();

        User seller = new User();

        for (User user1 : userService.findAll()) {
            if (user1.getId() == sellerId) {
                seller = user1;
                break;
            }
        }
        boolean haveItem = false;
        for (BackPack currentBackpack : backpack) {
            if (currentBackpack.getItemId() == itemId) {
                haveItem = true;
                myBackpack = currentBackpack;
                break;
            }
        }
        int currentQuantity = myBackpack.getQuantity();

        if (user.getId() == sellerId) {
            if (haveItem) {
                if (currentQuantity >= 1) {
                    myBackpack.setQuantity(currentQuantity + 1);
                    backpackService.saveBackpack(myBackpack);
                    modelAndView.addObject("buyerId", user.getId());


                    // to iter over backpackService
                    List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

                    Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);


                    // here to show backpack

                    int size = 3;

                    BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQuantity);


                    modelAndView.setViewName("user/backpack");
                    modelAndView.addObject("successBuyMessage", String.format("You return item successfully"));
                }

            } else {
                myBackpack.setPlayerId(user.getId());
                myBackpack.setQuantity(1);
                myBackpack.setItemId(itemId);
                backpackService.saveBackpack(myBackpack);
                Trade.visualizeTradeItems(modelAndView, tradeService);


                // to iter over backpackService
                List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

                Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);


                // here to show backpack

                int size = 3;

                BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQuantity);


                modelAndView.addObject("buyerId", user.getId());
                modelAndView.addObject("successBuyMessage", String.format("You return item successfully"));
                modelAndView.setViewName("user/backpack");

            }


            tradeService.deleteDeal(id);


//            backpackService.saveBackpack(myBackpack);
        } else {
            modelAndView.addObject("buyerId", user.getId());
            modelAndView.addObject("successBuyMessage", String.format("You can't return this item. He belong to other player."));
            Trade.visualizeTradeItems(modelAndView, tradeService);
            modelAndView.setViewName("user/backpack");
        }

        Trade.visualizeTradeItems(modelAndView, tradeService);

        User.visualizeUserStats(modelAndView, user, weaponService);
        modelAndView.addObject("buyerId", user.getId());

        modelAndView.setViewName("user/backpack");
        return modelAndView;
    }


}
