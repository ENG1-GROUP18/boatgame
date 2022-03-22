package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.screens.BasicMenuScreen;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class BasicMenuScreenTest {
    private BasicMenuScreen screen;
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
        screen = new BasicMenuScreen( boatGameInstance);
    }
    @Test
    public void basicMenuScreenCreated(){
        screen.resize(5,5);
        assertNotNull("Menu Screen not initialised",screen);
    }

    @Test
    public void disposeTest(){
        screen.dispose();
        assertNotNull("Menu Screen not initialised",screen);
    }



    @AfterClass
    public static void dispose(){
        app.exit();
        app = null;
    }
}