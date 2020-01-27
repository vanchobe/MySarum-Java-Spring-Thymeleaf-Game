package com.mysarum.service;

import com.mysarum.model.BackPack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BackpackService {

    public List<BackPack> findByPid(int playerId);


    public BackPack findByPlayerId(int playerId);

    public BackPack findById(int id);

    public BackPack saveBackpack(BackPack backPack);

    public void deleteFromBackpack(int id);

    public List<BackPack> findAll();

    public Page<BackPack> getPaginatedBackpack(Pageable pageable);


}

