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

public class PlayerTest {

    public static Player player;
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
        player = new Player(new World(new Vector2(0,0),true),new GameState());

    }

    @Test
    public void init(){
        assertNotNull("player object not initialised",player);
    }

    @Test
    public void getPosition() {
        assertEquals("Player not in the correct starting position",new Vector2(100,100),player.getPosition());
    }

    @Test
    public void movement(){
        //Need to figure out a way to test movement
        player.update(0.016f);
        assertEquals("Player did not move",new Vector2(100,100),player.getPosition());
    }

    @Test
    public void testBulletColor() {
        player.setBulletColor("Red");
        assertEquals("Function returns wrong value or setting bullet colour not working","Red",player.getBulletColor());
    }

    @Test
    public void testHealth() {
        player.setHealth(20f);
        assertEquals("Player health not set",20,(int)player.getHealth());
    }

    @Test
    public void testImmuneSeconds() {
        player.setImmuneSeconds(20);
        assertEquals("Player immunity seconds not set",20, player.getImmuneSeconds());
        player.update(0.016f); // Test update lines
    }


    @Test
    public void getMaxHealth() {
        assertEquals("Function returns incorrect value",100,(int)player.getMaxHealth());
    }

    @Test
    public void takeDamage() {
        player.takeDamage(15);
        assertEquals("Damage not taken",85,(int) player.getHealth());

    }

    @Test
    public void isDead() {
        assertFalse("Player should be alive",player.isDead());
        player.takeDamage(100);
        assertTrue("Player should be dead",player.isDead());
    }

    @Test
    public void isHit() {
        assertFalse(player.isHit());
    }

    @Test
    public void combat() {
        assertEquals("List not empty",new ArrayList<Bullet>(),player.combat());
    }

    @Test
    public void dispose() {
        player.dispose();
    }

    @Test
    public void scaleDamage() {
        float currentScaler =  player.getDamageScaler();
        player.scaleDamage(5f);
        assertEquals("Did not scale damage correctly",currentScaler*5f, player.getDamageScaler(),0.01f);
    }

    @Test
    public void getBody() {
        assertEquals(new Vector2(100,100),player.getBody().getPosition());
    }

    @Test
    public void updateState() {
        GameState newGameState = new GameState();
        player.setHealth(20);
        player.updateState(newGameState);
        assertEquals("Test if state holds correct data",20,(int) newGameState.currentHealth);
    }
}