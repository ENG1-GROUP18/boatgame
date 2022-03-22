package com.boatcorp.boatgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.awt.*;

import static org.junit.Assert.*;

public class BoatGameTest {

    private BoatGame boatGameInstance;
    public static Application app;

    @BeforeClass
    public static void setUp() {
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
        boatGameInstance = new BoatGame();
        boatGameInstance.setHeadless();
    }

    @Test
    public void verifyNoExceptionThrown(){

    }

    @Test
    public void create() {
        assertNotNull("Not initialised properly", boatGameInstance);
        assertNotNull("font files have been deleted", Gdx.files.internal("fonts/PressStart2P.fnt"));
        boatGameInstance.create();
    }

    @Ignore
    public void getVfxManager() {
        //Not tested as VfxManager can't be loaded in headless mode
    }

    @Test
    public void changeScreen(){
    }

    @Test
    public void saveGame() {

    }

    @Test
    public void loadGame() {
    }

    @AfterClass
    public static void dispose(){
        app.exit();
        app = null;
    }
}