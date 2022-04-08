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
        screen = new PlayScreen(boatGameInstance, new GameState());

    }

//    @Test
//    public void playScreenCreated(){
//        assertNull("Play screen not initialised",screen);
//    }
//
//    @Test
//    public void addColleges() {
//    }
//
//    @Test
//    public void setMode() {
//    }
//
//    @Test
//    public void upgradePlayer() {
//    }
//
//    @Test
//    public void handleMacros() {
//    }
//
//    @Test
//    public void freeze() {
//    }
//
//    @Test
//    public void unfreeze() {
//    }
//
//    @Test
//    public void loadBullets() {
//    }
//
//    @Test
//    public void handleBullets() {
//    }
//
//    @Test
//    public void addBullets() {
//    }
//
//    @Test
//    public void getState() {
//    }
}