package com.boatcorp.boatgame.frameworks;

/**
 * A class which holds and performs arithmetic on the players plunder.
 * Added in assessment 2 to help fulfil the requirement of spending plunder - USR25.
 */
public class PlunderSystem {
    private static float plunder;

    public static int getPlunder() {
        return (int) plunder;
    }
    
    public static void setPlunder(int newPlunder) {
        plunder = newPlunder;
    }

    public static void incrementPlunder(float amount) {
        plunder += amount;
    }
    
    public static void decrementPlunder(float amount) {
        plunder -= amount;
    }

    public static void update(final float delta) {
    }
}
