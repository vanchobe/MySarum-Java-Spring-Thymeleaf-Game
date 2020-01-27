package com.mysarum.service;

import com.mysarum.model.Food;
import com.mysarum.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public Food findById(int id) {
        return foodRepository.findById(id);
    }

    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }
}