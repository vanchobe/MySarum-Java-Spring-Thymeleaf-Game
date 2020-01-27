package com.mysarum.repository;

import com.mysarum.model.Dungeon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DungeonRepository extends JpaRepository<Dungeon, Integer> {
    public Dungeon findById(int id);
}
