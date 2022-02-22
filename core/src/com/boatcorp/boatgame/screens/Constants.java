package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.math.Vector2;
import com.crashinvaders.vfx.effects.CompositeVfxEffect;

public class Constants {

    private Constants() {
    }

    //TODO remove these or at the very least add them to the game class as static.

    public static final Vector2 GRAVITY = new Vector2(0, 0);
    public static final float DEFAULT_ZOOM = 16f;
    public static final float PPM = 25f;

    public static final String MAP_NAME = "Maps/map.tmx";
    public static final String BULLET_PATH = "Entities/bullet.png";

}

