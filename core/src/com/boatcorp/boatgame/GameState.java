package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

/**
* Creates game state used for intiliasing, saving and loading game.
*/

public class GameState{
    public Vector2 playerPosition = new Vector2(100,100);
    public float currentHealth = 100;
    public float maxHealth = 100;
    public int points = 0;
    public int plunder = 0;
    public int immuneSeconds = 0;
    public float damageScaler = 1f;
    public float shipDamageScaler = 1;
    public float[] collegeHealth = {100,100};
    public int difficulty = 1;
    public boolean hasBoughtGreen = false;
    public boolean hasBoughtRed = false;
    public boolean hasBoughtHealth = false;
    public String bulletColor = "bullet";
    public ArrayList<String> collegeNames = new ArrayList<>( Arrays.asList("langwith","james","goodricke","alcuin","derwent","halifax"));
    public Map<Object, float[]> collegeHealths = new HashMap<Object,float[]>();
    public Map<Object, Vector2> collegePositions = new HashMap<Object,Vector2>();
    public boolean isSpawn = true;
    public boolean shopUnlocked = false;
    }

