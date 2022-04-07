package com.boatcorp.boatgame;

import com.badlogic.gdx.math.Vector2;
import java.util.*;

/**
* Creates game state used for intiliasing, saving and loading game.
*/

public class GameState{
    //player info
    public Vector2 playerPosition = new Vector2(100,100);
    public float currentHealth = 100;
    public float maxHealth = 100;
    public int points = 0;
    public int plunder = 0;
    public int immuneSeconds = 0;
    public float damageScaler = 1f;
    public int difficulty = 1;
    public boolean hasBoughtGreen = false;
    public boolean hasBoughtRed = false;
    public boolean hasBoughtHealth = false;
    public String bulletColor = "bullet";
    public boolean isSpawn = true;
    public boolean shopUnlocked = false;
    public boolean isFrozen = true;
    public long timeSinceFreeze = 0;

    //ship info
    public float shipDamageScaler = 1;
    public ArrayList<Float> shipHealths = new ArrayList<>();
    public ArrayList<Vector2> shipPositions = new ArrayList<>();
    public ArrayList<Vector2> shipStartPositions = new ArrayList<>();
    public ArrayList<Long> shipTimes = new ArrayList<>();

    //monster info
    public ArrayList<Float> monsterHealths = new ArrayList<>();
    public ArrayList<Vector2> monsterPositions = new ArrayList<>();
    public ArrayList<Vector2> monsterStartPositions = new ArrayList<>();

    //college info
    public float[] collegeHealth = {100,100};
    public ArrayList<String> collegeNames = new ArrayList<>( Arrays.asList("langwith","james","goodricke","alcuin","derwent","halifax"));
    public Map<Object, float[]> collegeHealths = new HashMap<>();
    public Map<Object, Vector2> collegePositions = new HashMap<>();
    public Map<Object, Long> collegeTimes = new HashMap<>();

    //bullet info
    public ArrayList<String> bulletColors = new ArrayList<>();
    public ArrayList<String> firedFroms = new ArrayList<>();
    public ArrayList<Vector2> velocities = new ArrayList<>();
    public ArrayList<Vector2> positions = new ArrayList<>();

    //test info
    public boolean headless = false;
    }

