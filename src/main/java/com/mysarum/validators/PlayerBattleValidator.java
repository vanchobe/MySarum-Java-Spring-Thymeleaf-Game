package com.mysarum.validators;

public class PlayerBattleValidator {
    public static boolean validatePlayerPower(int attackerPower, int defenderPower) {
        if (attackerPower >= defenderPower) {
            return true;
        } else {
            return false;

        }
    }

    public static boolean validateAttackerMana(int attackerMana) {
        if (attackerMana >= 50) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateAttackerHealth(int attackerHealth) {
        if (attackerHealth >= 25) {
            return true;
        } else {
            return false;

        }
    }
    public static boolean validatePlayerLevel(int attackerLevel, int defenderLevel) {
        if (attackerLevel >= defenderLevel) {
            return true;
        } else {
            return false;

        }
    }
    public static boolean validateDefenderHealth(int defenderHealth) {
        if (defenderHealth >= 30) {
            return true;
        } else {
            return false;

        }
    }


    public static boolean validateDefenderMana(int defenderMana) {
        if (defenderMana >= 10) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateDefenderGold(int defenderGold) {
        if (defenderGold >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validatePlayersIds(int attackerId, int defenderId) {
        if (attackerId != defenderId) {
            return true;
        } else {
            return false;

        }
    }

}
