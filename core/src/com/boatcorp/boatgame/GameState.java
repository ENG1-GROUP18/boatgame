package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameState {
    private Vector2 playerPosition = new Vector2(100,100);
    private int points;

    public GameState() {
        this(new Vector2(100,100), 0);
    }
    public GameState(Vector2 playerPosition, int points){
        this.playerPosition = playerPosition;
        this.points = points;
    }

    public void setPlayerPosition(Vector2 newPosition){
        playerPosition = newPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

    public int getPoints() {
        return points;
    }
}
