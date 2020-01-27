package com.mysarum.service;

import com.mysarum.model.Dungeon;
import com.mysarum.repository.DungeonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DungeonServiceImpl implements DungeonService {

    private DungeonRepository dungeonRepository;

    @Autowired
    public DungeonServiceImpl(DungeonRepository dungeonRepository) {
        this.dungeonRepository = dungeonRepository;
    }

    @Override
    public Dungeon findById(int id) {
        return dungeonRepository.findById(id);
    }

    @Override
    public List<Dungeon> findAll() {
       return dungeonRepository.findAll();
    }
}
