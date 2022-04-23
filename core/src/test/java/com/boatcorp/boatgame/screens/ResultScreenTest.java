package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.boatcorp.boatgame.BoatGame;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;

public class ResultScreenTest{
    private ResultScreen screen;
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
        Boolean win = true;
        screen = new ResultScreen(win, boatGameInstance);
    }

    @Test
    public void ResultScreenCreated(){
        screen.resize(5,5);
        assertNotNull("Pause screen not initialised",screen);
    }

    @Test
    public void disposeTest(){
        screen.dispose();
        assertNotNull("Pause Screen not initialised",screen);
    }

    @AfterClass
    public static void tearDown() {
        app.exit();
        app = null;
    }
}
