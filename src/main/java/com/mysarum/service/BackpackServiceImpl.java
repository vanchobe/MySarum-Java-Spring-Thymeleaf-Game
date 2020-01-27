package com.mysarum.service;

import com.mysarum.model.BackPack;
import com.mysarum.repository.BackpackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackpackServiceImpl implements BackpackService {

    private BackpackRepository backpackRepository;

    @Autowired
    public BackpackServiceImpl(BackpackRepository backpackRepository) {
        this.backpackRepository = backpackRepository;
    }


    @Override
    public BackPack findByPlayerId(int playerId) {
        return backpackRepository.findByPlayerId(playerId);
    }

    @Override
    public BackPack findById(int id) {
        return backpackRepository.findById(id);
    }


//    @Override
//    public Work findWorkingPlayerId(int id) {
//        return workRepository.findWorkingPlayerId(id);
//    }

    @Override
    public BackPack saveBackpack(BackPack backPack) {
        return backpackRepository.save(backPack);
    }

    @Override
    public void deleteFromBackpack(int id) {
        backpackRepository.delete(findById(id));
    }

    @Override
    public List<BackPack> findAll() {
        return backpackRepository.findAll();
    }


    @Override
    public List<BackPack> findByPid(int playerId) {

        List<BackPack> backPack = new ArrayList<>();
        for (BackPack currentBackPack : backpackRepository.findAll()) {
            if (playerId == currentBackPack.getPlayerId()) {
                backPack.add(currentBackPack);
            }
        }
        return backPack;
    }


    public Page<BackPack> getPaginatedBackpack(Pageable pageable) {
        return backpackRepository.findAll(pageable);
    }


}