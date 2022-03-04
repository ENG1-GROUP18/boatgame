package com.boatcorp.boatgame;
import com.badlogic.gdx.math.Vector2;

public class GameState {
    private Vector2 playerPosition;
    
    public void setPlayerPosition(Vector2 newPosition){
        playerPosition = newPosition;
    }

    public Vector2 getPlayerPosition() {
        return playerPosition;
    }
    public void initialise(){
        playerPosition = new Vector2(100,100);
    }
}
