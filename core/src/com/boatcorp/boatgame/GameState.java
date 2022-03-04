package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

public class GameState {
    public Vector2 playerPosition = new Vector2(100,100);
    public float currentHealth;
    public float maxHealth;
    public int points;

    public GameState() {
        this(new Vector2(100,100),100,100, 0);
    }
    public GameState(Vector2 playerPosition,float currentHealth, float maxHealth, int points){
        this.playerPosition = playerPosition;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.points = points;

    }
}
