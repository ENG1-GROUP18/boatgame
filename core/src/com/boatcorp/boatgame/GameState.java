package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

public class GameState {
    private Vector2 playerPosition = new Vector2(100,100);
    private float currentHealth;
    private float maxHealth;
    private int points;

    public GameState() {
        this(new Vector2(100,100),100,100, 0);
    }
    public GameState(Vector2 playerPosition,float currentHealth, float maxHealth, int points){
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
