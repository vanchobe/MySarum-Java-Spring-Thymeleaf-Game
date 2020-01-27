package com.mysarum.service;

import com.mysarum.model.Dungeon;

import java.util.List;

public interface DungeonService {

    public Dungeon findById(int id);

    public List<Dungeon> findAll();

}
