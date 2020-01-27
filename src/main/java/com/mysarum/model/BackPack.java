package com.mysarum.model;

import com.mysarum.service.BackpackService;
import com.mysarum.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BACKPACK")
public class BackPack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BACKPACK_ID")
    private int id;
    @Column(name = "PLAYER_ID")
    private int playerId;
    @Column(name = "ITEM_ID")
    private int itemId;
    @Column(name = "ITEM_QUANTITY")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public static List<Item> showBackpackItems(BackpackService backpackService, ItemService itemService, User user) {
        List<Item> sortedList = new ArrayList<>();
        for (BackPack backPack : backpackService.findAll()) {

            for (Item item : itemService.findAll()) {
                if (item.getId() == backPack.getItemId()
                        && backPack.getPlayerId() == (user.getId())
                ) {
                    sortedList.add(itemService.findItemById(item.getId()));


                }
            }


        }
        return sortedList;

    }


    public static Map<String, Integer> showBackpackQuantity(List<Item> sortedList, BackpackService backpackService, User user) {

        Map<String, Integer> quantityMap = new HashMap<>();
        for (BackPack backPack : backpackService.findAll()) {


            for (Item item : sortedList) {
                if (item.getId() == backPack.getItemId()
                        && backPack.getPlayerId() == (user.getId())) {
                    quantityMap.put(item.getName(), backPack.getQuantity());
                }
            }


        }
        return quantityMap;

    }

    public static void showBackpackItemsPaging(int size,int page, BackpackService backpackService, List<Item> sortedList, ModelAndView modelAndView,User user,Map<String, Integer> itemQuantity){


        PageRequest pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<BackPack> myBackpack = backpackService.getPaginatedBackpack(pageable);
        Page<Item> items = new PageImpl<Item>(sortedList, pageable, sortedList.size());


        int totalPages = items.getTotalPages();

        if (totalPages > 0 && page != size) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);

        }
        if (page == size) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, sortedList.size()/3 + 1).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("playerId", user.getId());
        modelAndView.addObject("backpacks", myBackpack);

        modelAndView.addObject("items", sortedList);


        modelAndView.addObject("quantity", itemQuantity);

    }


}