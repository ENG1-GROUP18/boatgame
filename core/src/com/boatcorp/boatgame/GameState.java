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
    public float[] collegeHealth = {100,100};
    public ArrayList<String> collegeNames = new ArrayList<>( Arrays.asList("langwith","james","goodricke"));
    public Map<Object, float[]> collegeHealths = new HashMap<Object,float[]>();
    public Map<Object, Vector2> collegePositions = new HashMap<Object,Vector2>();
    public boolean isSpawn = true;
    }
