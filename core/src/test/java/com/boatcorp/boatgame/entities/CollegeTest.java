package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class CollegeTest {

    public static College collage;
    public static Application app;
    private static GameState state;
    @BeforeClass
    public static void setUp(){
        //Any set up for tests go here
        app = new HeadlessApplication(
                new ApplicationListener() {
                    @Override
                    public void create() {}
                    @Override
                    public void resize(int width, int height) {}
                    @Override
                    public void render() {}
                    @Override
                    public void pause() {}
                    @Override
                    public void resume() {}
                    @Override
                    public void dispose() {}
                }
        );
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
        Gdx.graphics = Mockito.mock(com.badlogic.gdx.Graphics.class);

    }

    @Before
    public void start(){
        BoatGame boatGameInstance = new BoatGame();
        boatGameInstance.setHeadless();
        state = new GameState();
        state.headless = true;

        state.collegeHealths.put(state.collegeNames.get(0), state.collegeHealth);
        state.collegePositions.put(state.collegeNames.get(0), new Vector2(200, 200));
        collage = new College(state.collegeNames.get(0), new World(new Vector2(0,0),true),state);

    }




    @Test
    public void getPosition() {
        assertEquals("Is the start position correct",new Vector2(200,200),collage.getPosition());
    }

    @Test
    public void combat() {
        ArrayList<Bullet> bullets =  collage.combat(new Player(new World(new Vector2(0,0),true),state));
        assertEquals("Is the array of bullets empty",new ArrayList<Bullet>() ,bullets);
    }

    @Test
    public void draw() {
        collage.draw();
        //Not sure if to keep this test as it doesn't tell us anything, just that the function runs
    }


    @Test
    public void isAlive() {
        assertTrue("Is collage alive",collage.isAlive());
        collage.takeDamage(100);
        assertFalse("Is collage dead",collage.isAlive());
    }

    @Test
    public void takeDamage() {
        int health = (int) collage.getHealth();
        collage.takeDamage(5);
        assertEquals("Health not decreased by correct amount",health-5,(int)collage.getHealth());
    }

    @Test // Not needed, but still testable
    public void setMatrix() {
        collage.setMatrix(new Matrix4());
    }

    @Test
    public void dispose() {
        collage.dispose();
    }

    @Test
    public void getUserData() {
        assertEquals("langwith", collage.getUserData());
    }

    @Test
    public void updateState() {
        GameState newState = new GameState();
        int health = (int)collage.getHealth();
        collage.takeDamage(5);
        collage.updateState(newState);
        assertEquals("Update state",health-5,(int) newState.collegeHealths.get("langwith")[0]);

    }

    @Test
    public void scaleDamage() {
        assertEquals("Is starting scale damage 1",1, (int) collage.getDamageScaler());
        collage.scaleDamage(5);
        assertEquals("Is the scalled damaged scalled by the right amount",5, (int)collage.getDamageScaler());
    }
}