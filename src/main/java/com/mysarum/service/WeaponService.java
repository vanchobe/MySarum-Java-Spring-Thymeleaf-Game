package com.mysarum.service;

import com.mysarum.model.Weapon;

import java.util.List;

public interface WeaponService {

    public Weapon findWeaponById(int id);

    public List<Weapon> findAll();

}
