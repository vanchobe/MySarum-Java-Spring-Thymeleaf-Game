package com.mysarum.repository;

import com.mysarum.model.BackPack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackpackRepository extends JpaRepository<BackPack, Integer> {
    public BackPack findByPlayerId(int playerId);
    public BackPack findById(int id);

    Page<BackPack> findAll(Pageable pageable);


}