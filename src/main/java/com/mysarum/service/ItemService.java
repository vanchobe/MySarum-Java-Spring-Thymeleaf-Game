package com.mysarum.service;

import com.mysarum.model.Item;

import java.util.List;

public interface ItemService {

    public Item findItemById(int id);

    public List<Item> findAll();





}

