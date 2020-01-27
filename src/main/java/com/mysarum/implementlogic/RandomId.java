package com.mysarum.implementlogic;

public class RandomId {
    private static final int RANDOM_WORK_RANGE = 15;

    private static final int RANDOM_ATTACK_RANGE = 40;


    public static int generateRandomWorkId(int bonusDrop) {

        java.util.Random rand = new java.util.Random();


        return rand.nextInt((RandomId.RANDOM_WORK_RANGE) / bonusDrop);
    }

    public static int generateRandomAttack(int bonusDrop) {


        java.util.Random rand = new java.util.Random();
        return rand.nextInt((RandomId.RANDOM_ATTACK_RANGE) / bonusDrop);
    }
}
