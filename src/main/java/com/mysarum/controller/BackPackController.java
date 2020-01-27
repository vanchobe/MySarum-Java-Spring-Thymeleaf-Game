package com.mysarum.controller;

import com.mysarum.model.BackPack;
import com.mysarum.model.Item;
import com.mysarum.model.Trade;
import com.mysarum.model.User;
import com.mysarum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BackPackController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private DungeonService dungeonService;

    @Autowired
    private BackpackService backpackService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private ItemService itemService;


    @RequestMapping(value = "/user/backPack", method = RequestMethod.GET)
    public ModelAndView backpackHome() {
        ModelAndView modelAndView = new ModelAndView("backpack-items-paging");
        int page = 1;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);
        // to iter over backpackService


        Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);

        int totalPages = -1;
        int size = 3;
        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<BackPack> myBackpack = backpackService.getPaginatedBackpack(pageable);
        Page<Item> items = new PageImpl<Item>(sortedList, pageable, sortedList.size());


        totalPages = items.getTotalPages();

        // totalPages > 0 && page <= Math.ceil(totalPages / size) || totalPages == 1
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);

        }
        modelAndView.addObject("playerId", user.getId());
        //        modelAndView.addObject("backpacks", myBackpack);

        modelAndView.addObject("items", sortedList);


        modelAndView.addObject("quantity", itemQuantity);


        User.visualizeUserStats(modelAndView, user, weaponService);

        Trade.visualizeTradeItems(modelAndView, tradeService);


        modelAndView.setViewName("user/backPack");
        return modelAndView;
    }


    @RequestMapping(value = "/user/backPack/{page}", method = RequestMethod.GET)
    public ModelAndView backpack(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("backpack-items-paging");


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);


        Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);

        //  items per page
        int size = 3;

        BackPack.showBackpackItemsPaging(size, page, backpackService, sortedList, modelAndView, user, itemQuantity);


        // to show items from my players backpack

        User.visualizeUserStats(modelAndView, user, weaponService);

        Trade.visualizeTradeItems(modelAndView, tradeService);


        modelAndView.setViewName("user/backPack");
        return modelAndView;
    }

    @RequestMapping(value = "/sell-item/{id}", method = RequestMethod.POST)
    public ModelAndView sellItem(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        // to iter over backpackService
        List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);


        Map<String, Integer> itemQty = BackPack.showBackpackQuantity(sortedList, backpackService, user);

        // here to show backpack


        boolean isContained = false;

        int backpackIdForRemove = 0;
        int itemQuantity = 0;


        for (BackPack backPack : backpackService.findAll()) {

//            for (Item item : sortedList) {
            if (id == backPack.getItemId()
                    && backPack.getPlayerId() == (user.getId())
            ) {
                isContained = true;
                backpackIdForRemove = backPack.getId();
                itemQuantity = backPack.getQuantity();
                break;


            }
//            }
        }


        if (isContained) {


            int itemSellPrice = itemService.findItemById(id).getSellPrice();
            int experienceEarn = itemService.findItemById(id).getExperienceEarn();


            int playerGold = user.getGold();
            int playerExperience = user.getExperience();

            user.setGold(playerGold + itemSellPrice);
            user.setExperience(playerExperience + experienceEarn);


            if (itemQuantity > 1) {
                BackPack backpack = backpackService.findById(backpackIdForRemove);
                backpack.setQuantity(itemQuantity - 1);
                backpackService.saveBackpack(backpack);
                itemQty = BackPack.showBackpackQuantity(sortedList, backpackService, user);

            } else {
                backpackService.deleteFromBackpack(backpackIdForRemove);
            }


            userService.saveStats(user);

            for (BackPack backPack : backpackService.findAll()) {

                for (Item item : itemService.findAll()) {
                    if (id == backPack.getItemId()
                            && backPack.getPlayerId() == (user.getId())
                    ) {
                        sortedList.add(itemService.findItemById(item.getId()));


                    }
                }


            }

            User.visualizeUserStats(modelAndView, user, weaponService);
            sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

//            modelAndView.addObject("items", sortedList);
//
//            modelAndView.addObject("quantity", itemQty);

            //  items per page
            int size = 3;

            BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQty);


            modelAndView.addObject("successAttackMessage", String.format("You sell item successfully!"));
            modelAndView.setViewName("user/backPack");

        } else {


            modelAndView.addObject("successAttackMessage", String.format("You don't have this item!"));
            User.visualizeUserStats(modelAndView, user, weaponService);


            List<Item> emptyList = new ArrayList<>();

            Map<String, Integer> emptyMap = new HashMap<>();

            modelAndView.addObject("quantity", emptyMap);


            modelAndView.addObject("items", emptyList);

            modelAndView.setViewName("user/backPack");


        }


        return modelAndView;
    }

    @RequestMapping(value = "/user/itemtrade/{id}", method = RequestMethod.POST)
    public ModelAndView tradeItem(@PathVariable("id") int id, @ModelAttribute("tradeInfo") Trade tradeInfo) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());


        int playerGold = user.getGold();

        List<Item> sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

        int itemId = id;
        String itemName = "";
        String itemImage = "";
        int itemPrice = 0;
        int sellerId = user.getId();
        String sellerName = user.getName();

        boolean isContained = false;
        for (Item item : sortedList) {
            if (item.getId() == id) {
                itemName = item.getName();
                itemImage = item.getItemImage();
                itemPrice = item.getSellPrice();

                isContained = true;
                break;
            }
        }


        if (isContained && (tradeInfo.getPrice() > 0)) {


            tradeInfo.setItemId(itemId);
            tradeInfo.setName(itemName);
            tradeInfo.setImage(itemImage);
            tradeInfo.setSellerId(sellerId);
            tradeInfo.setPrice(tradeInfo.getPrice());
            tradeInfo.setSellerName(sellerName);

            List<BackPack> backPack = backpackService.findByPid(user.getId());


            int itemQuantity = 0;
            int backPackId = 0;


            for (BackPack pack : backPack) {
                if (pack.getItemId() == itemId) {
                    itemQuantity = pack.getQuantity();
                    backPackId = pack.getId();
                }

            }
            BackPack modifyBackpack = backpackService.findById(backPackId);
            int currentQuantity = modifyBackpack.getQuantity();
            if (itemQuantity > 1) {
                modifyBackpack.setQuantity(currentQuantity - 1);
                backpackService.saveBackpack(modifyBackpack);
            } else {
                backpackService.deleteFromBackpack(backPackId);
            }


            tradeService.saveDeal(tradeInfo);


            userService.saveStats(user);


            // to iter over backpackService


            Map<String, Integer> itemQuantitys = BackPack.showBackpackQuantity(sortedList, backpackService, user);


            // here to show backpack


            Map<String, Integer> itemQty = BackPack.showBackpackQuantity(sortedList, backpackService, user);

            int size = 3;

            BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQty);


            Trade.visualizeTradeItems(modelAndView, tradeService);

            User.visualizeUserStats(modelAndView, user, weaponService);

            modelAndView.addObject("successBuyMessage", String.format("Item was added to item market!"));

            modelAndView.setViewName("user/backpack");
        } else {


            User.visualizeUserStats(modelAndView, user, weaponService);

            // to iter over backpackService
            sortedList = BackPack.showBackpackItems(backpackService, itemService, user);

            Map<String, Integer> itemQuantity = BackPack.showBackpackQuantity(sortedList, backpackService, user);


            // here to show backpack

            int size = 3;

            BackPack.showBackpackItemsPaging(size, 1, backpackService, sortedList, modelAndView, user, itemQuantity);




            modelAndView.addObject("successAttackMessage", String.format("You don't have this item, or price must be positive and can't be zero"));
            modelAndView.setViewName("user/backPack");

        }


        return modelAndView;
    }

}
