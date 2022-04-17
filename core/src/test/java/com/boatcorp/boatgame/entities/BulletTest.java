package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
public class BulletTest {

    public static Bullet bullet;
    public static Application app;
    private static GameState state;

    @BeforeClass
    public static void setUp() {
        //Any set up for tests go here
        app = new HeadlessApplication(
                new ApplicationListener() {
                    @Override
                    public void create() {
                    }

                    @Override
                    public void resize(int width, int height) {
                    }

                    @Override
                    public void render() {
                    }

                    @Override
                    public void pause() {
                    }

                    @Override
                    public void resume() {
                    }

                    @Override
                    public void dispose() {
                    }
                }
        );
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
        Gdx.graphics = Mockito.mock(com.badlogic.gdx.Graphics.class);

    }

    @Before
    public void start() {
        BoatGame boatGameInstance = new BoatGame();
        boatGameInstance.setHeadless();
        state = new GameState();
        state.headless = true;

        Vector2 position = new Vector2(0, 0);
        Vector2 velocity = new Vector2(10, 10); //x - x axis positive up and negative down for y vice versa
        String firedFrom = "Player";
        String color = "redbullet";
        World world = new World(new Vector2(0, 0), true);

        bullet = new Bullet(position, velocity, world, firedFrom, color, state);
        //    public Bullet(Vector2 position, Vector2 velocity, World world, String firedFrom, String color, GameState state)
    }

    //this test not needed
    @Test
    public void testGetFiredFrom() {
        assertEquals("Check if fired from correct object","Player",bullet.getFiredFrom());
    }

    @Test
    public void testOutOfRange() {
        assertEquals("Is not out of range",true, bullet.outOfRange(1));
        assertEquals("Is out of range",false, bullet.outOfRange(100));
    }

    @Test
    public void testHit() {
        assertFalse(bullet.hit());
        //assertEquals("Bullet didn't hit",true, bullet.hit());
    }

    @Test
    public void testMove() {
        bullet.move(2f);
        assertEquals("Move not made",new Vector2(5, 5),bullet.getVelocity());
    }

    @Test
    public void testDispose() {
        bullet.dispose();
    }

    @Test
    public void testUpdateState() {
        GameState newGameState = new GameState();
        bullet.move(2f);
        bullet.updateState(newGameState);
        assertEquals("Game state saved current state",new Vector2(5, 5),newGameState.velocities.get(0));


    }
}