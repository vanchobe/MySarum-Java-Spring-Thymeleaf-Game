package com.mysarum.service;

import com.mysarum.model.Weapon;
import com.mysarum.repository.WeaponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeaponServiceImpl implements WeaponService {

    private WeaponRepository weaponRepository;

    @Autowired
    public WeaponServiceImpl(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }

    @Override
    public Weapon findWeaponById(int id) {
        return weaponRepository.findById(id);
    }

    @Override
    public List<Weapon> findAll() {
        return weaponRepository.findAll();
    }
}
