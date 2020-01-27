package com.mysarum.validators;

public class WeaponShopValidator {
    public static boolean validatePlayerLevel(int playerLevel, int levelRequired) {
        if (playerLevel >= levelRequired) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validatePlayerGold(int playerGold, int weaponPrice) {
        if (playerGold >= weaponPrice) {
            return true;

        } else {
            return false;

        }
    }

    public static boolean validateHaveSameWeapon(int weapon, int id) {
        if (weapon != id) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validateHaveBetterWeapon(int weapon, int id) {
        if (weapon < id) {
            return true;
        } else {
            return false;

        }
    }
}
