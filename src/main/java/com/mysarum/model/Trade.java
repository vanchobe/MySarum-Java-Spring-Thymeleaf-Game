package com.mysarum.model;

import com.mysarum.service.TradeService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRADE")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;
    @Column(name = "ITEM_ID")
    private int itemId;
    @Column(name = "ITEM_NAME")
    private String name;
    @Column(name = "ITEM_IMAGE")
    private String image;
    @Column(name = "ITEM_PRICE")
    private int price;
    @Column(name = "ITEM_SELLER")
    private int sellerId;
    @Column(name = "ITEM_SELLER_NAME")
    private String sellerName;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public static void visualizeTradeItems(ModelAndView modelAndView, TradeService tradeService, Page tradePage) {

        modelAndView.addObject("tradeInfo", tradePage.getContent());
    }

    public static void visualizeTradeItems(ModelAndView modelAndView, TradeService tradeService) {

        modelAndView.addObject("tradeInfo", tradeService.findAll());
    }


    public static void visualizeTradeItemsPaging(int page,TradeService tradeService,ModelAndView modelAndView,User user) {
        PageRequest pageable = PageRequest.of(page - 1, 4, Sort.by("id").descending());

        Page<Trade> tradePage = tradeService.getPaginatedItems(pageable);

        int totalPages = tradePage.getTotalPages();

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }

        modelAndView.addObject("tradeInfo", tradePage.getContent());


        modelAndView.addObject("buyerId", user.getId());
    }

}