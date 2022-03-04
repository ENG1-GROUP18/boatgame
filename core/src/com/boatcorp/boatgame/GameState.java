package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

public class GameState {
    private Vector2 playerPosition = new Vector2(100,100);
    private int currentHealth;
    private float maxHealth;
    private float points;

    public GameState() {
        this(new Vector2(100,100),100,100, 0);
    }
    public GameState(Vector2 playerPosition,int currentHealth, int maxHealth, int points){
        this.playerPosition = playerPosition;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.points = points;

    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public int getPoints() {
        return points;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getMaxHealth(){
        return currentHealth;
    }
}
