package com.mysarum.validators;

public class FoodShopValidator {
    public static boolean validatePlayerLevel(int playerLevel, int levelRequired) {
        if (playerLevel >= levelRequired) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validatePlayerGold(int playerGold, int foodPrice) {
        if (playerGold - foodPrice >= 0) {
            return true;

        } else {
            return false;

        }
    }
}
