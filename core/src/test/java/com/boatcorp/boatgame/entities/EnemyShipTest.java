package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EnemyShipTest {
    public static EnemyShip ship;
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

        state.shipStartPositions.add(new Vector2(200,200));
        state.shipHealths.add(20f);
        state.shipPositions.add(new Vector2(200,200));
        state.shipTimes.add(0L);
        state.shipDamageScaler = 5;

        World world = new World(new Vector2(0,0),true);

        ship = new EnemyShip(world,state,new Player(world,state),0);

    }

    @Test
    public void shoot() {
        //Not a great test but just for example
        //This will also increase the lines covered
        assertEquals("Checks if initial array list if empty",new ArrayList<Bullet>() , ship.shoot());
    }

    @Test
    public void freeze() {
    }

    @Test
    public void unfreeze() {
    }

    @Test
    public void isHit() {
    }

    @Test
    public void takeDamage() {

    }

    @Test
    public void scaleDamage() {
    }

    @Test
    public void getDamageScaler() {
    }

    @Test
    public void isAlive() {
    }

    @Test
    public void updateState() {
    }

    @Test
    public void dispose() {
    }
}