package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class PlayScreenTest {

    private PlayScreen screen;
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
        boatGameInstance.changeScreen(BoatGame.screenType.MODE);
        state = new GameState();
        state.headless = true;
        screen = new PlayScreen(boatGameInstance, state);

    }

    @Test
    public void playScreenCreated(){
        assertTrue("Check if Play screen initialised",screen instanceof Screen);
    }

    @Test
    public void render(){
        screen.render(0.016f);
    }



    @Test
    public void setMode() {
        screen.setMode(0);
        assertEquals(0.7f,screen.getPlayer().getDamageScaler(),0);
        screen.setMode(2);
        assertEquals(1.3f,screen.getPlayer().getDamageScaler(),0.1f);
    }

    @Test
    public void upgradePlayer() {
        //Need to figure out a way to test this
        screen.upgradePlayer(0);
        screen.upgradePlayer(1);
        screen.upgradePlayer(2);
        screen.upgradePlayer(3);
        screen.upgradePlayer(4);

    }

    @Test
    public void handleMacros() {
        //Need to figure out a way to test this
        screen.handleMacros();
    }


    @Test
    public void freeze() {
        screen.freeze();
        assertEquals("Check if ships are frozen",true,screen.getEnemyShips().get(0).get_freeze());
    }

    @Test
    public void unfreeze() {
        screen.unfreeze();
        assertEquals("Check if ships are unfrozen",false,screen.getEnemyShips().get(0).get_freeze());
    }


    @Test
    public void handleBullets() {
        screen.handleBullets();
    }

    @Test
    public void addBullets() {
        screen.addBullets();
    }

    @Test
    public void getState() {
        screen.freeze();
        GameState newState = screen.getState();
        assertEquals("Check if state has saved new info", true,newState.isFrozen);
    }

    @Test
    public void dispose(){
        screen.dispose();
    }
}