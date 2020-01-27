package com.mysarum.service;

import com.mysarum.model.Food;

import java.util.List;

public interface FoodService {

    public Food findById(int id);

    public List<Food> findAll();

}
