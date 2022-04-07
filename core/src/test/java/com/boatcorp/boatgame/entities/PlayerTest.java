package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;
import org.graalvm.compiler.word.WordOperationPlugin;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.event.KeyEvent;

import static org.junit.Assert.*;

public class PlayerTest {

    public static Player player;
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
        GameState state = new GameState();
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
        player.update(0.016f);
        assertEquals("Player did not move",new Vector2(100,100),player.getPosition());
    }

//    @Test
//    public void setBulletColor() {
//    }
//
//    @Test
//    public void getBulletColor() {
//    }
//
//    @Test
//    public void getHealth() {
//    }
//
//    @Test
//    public void setHealth() {
//    }
//
//    @Test
//    public void getImmuneSeconds() {
//    }
//
//    @Test
//    public void setImmuneSeconds() {
//    }
//
//    @Test
//    public void getMaxHealth() {
//    }
//
//    @Test
//    public void takeDamage() {
//    }
//
//    @Test
//    public void isDead() {
//    }
//
//    @Test
//    public void isHit() {
//    }
//
//    @Test
//    public void combat() {
//    }
//
//    @Test
//    public void dispose() {
//    }
//
//    @Test
//    public void scaleDamage() {
//    }
//
//    @Test
//    public void getBody() {
//    }
//
//    @Test
//    public void updateState() {
//    }
}