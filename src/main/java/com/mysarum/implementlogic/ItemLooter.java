package com.mysarum.implementlogic;

import com.mysarum.model.BackPack;
import com.mysarum.model.User;
import com.mysarum.service.BackpackService;
import com.mysarum.service.ItemService;
import org.springframework.web.servlet.ModelAndView;

public class ItemLooter {

    public static void loot(int randomId, ModelAndView modelAndView, ItemService itemService, BackpackService backpackService, User user) {
        BackPack backPack = new BackPack();

        boolean isContained = false;
        int backPackId = 0;
        int generatedId = randomId;
        if (itemService.findItemById(generatedId) != null) {

            BackPack playerBackpack = new BackPack();
            for (BackPack pack : backpackService.findAll()) {
                if (generatedId == pack.getItemId()
                        && pack.getPlayerId() == (user.getId())) {

                    playerBackpack = pack;
                    backPackId = pack.getId();
                    isContained = true;
                    break;
                }
            }


            if (isContained) {
                int currentQty = playerBackpack.getQuantity();
                backPack.setId(backPackId);
                backPack.setQuantity(currentQty + 1);
            } else {
                backPack.setQuantity(1);

            }
            backPack.setItemId(generatedId);

            backPack.setPlayerId(user.getId());

            backpackService.saveBackpack(backPack);


            modelAndView.addObject("successItemReceived", String.format("Today is lucky day!. You found %s. Check Backpack.", itemService.findItemById(generatedId).getName()));
        }

    }
}
