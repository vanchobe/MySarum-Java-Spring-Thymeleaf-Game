package com.mysarum.validators;

public class DungeonValidator {
    public static boolean validatePlayerPower(int userPower, int weaponPoints, int monsterPower) {
        if (userPower + weaponPoints >= monsterPower) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validatePlayerHealth(int playerHealth) {
        if (playerHealth > 0) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validatePlayerVsMonsterHealth(int playerHealth, int monsterHealth) {
        if (playerHealth >= monsterHealth) {

            return true;
        } else {
            return false;

        }
    }

    public static boolean validatePlayerLevel(int playerLevel, int levelRequired) {
        if (playerLevel >= levelRequired) {
            return true;

        } else {
            return false;

        }
    }

    public static boolean validatePlayerMana(int playerMana, int i) {
        if (playerMana >= i) {
            return true;

        } else {
            return false;

        }
    }
}
