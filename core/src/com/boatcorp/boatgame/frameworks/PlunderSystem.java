package com.boatcorp.boatgame.frameworks;

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
