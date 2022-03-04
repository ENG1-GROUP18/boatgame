package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

public class GameState {
    private Vector2 playerPosition;

    public GameState() {
        this(new Vector2(100,100));
    }
    public GameState(Vector2 playerPosition){
        this.playerPosition = playerPosition;
    }

    public void setPlayerPosition(Vector2 newPosition){
        playerPosition = newPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }

}
