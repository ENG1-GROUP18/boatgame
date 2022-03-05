package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class GameState{
    public Vector2 playerPosition = new Vector2(100,100);
    public float currentHealth = 100;
    public float maxHealth = 100;
    public int points = 0;
    public float[] collegeHealth = {100,100};
    Random rand = new Random();
    public List<String> collegeNames = Arrays.asList("langwith","james","goodricke");
    public Map<Object, float[]> collegeHealths = new HashMap<Object,float[]>();
    public Map<Object, Vector2> collegePositions = new HashMap<Object,Vector2>();
    public boolean isSpawn = true;
    }
