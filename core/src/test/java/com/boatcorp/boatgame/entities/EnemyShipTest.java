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
    public void start() {
        BoatGame boatGameInstance = new BoatGame();
        boatGameInstance.setHeadless();
        state = new GameState();
        state.headless = true;

        state.shipStartPositions.add(new Vector2(200, 200));
        state.shipHealths.add(20f);
        state.shipPositions.add(new Vector2(200, 200));
        state.shipTimes.add(0L);
        state.shipDamageScaler = 5;

        World world = new World(new Vector2(0, 0), true);

        ship = new EnemyShip(world, state, new Player(world, state), 0);

    }

    @Test
    public void testShoot() {
        ArrayList<Bullet> bullets = ship.shoot();
        assertEquals("Is the array of bullets empty", new ArrayList<Bullet>(), bullets);
    }

    @Test
    public void testFreeze() {
        ship.freeze();
        boolean freeze_stat = ship.get_freeze();
        assertEquals("Enemy ship not frozen",true, freeze_stat);
    }

    @Test
    public void testUnfreeze() {
        ship.unfreeze();
        boolean freeze_stat = ship.get_freeze();
        assertEquals("Enemy ship not unfrozen",false, freeze_stat);
    }

    @Test
    public void testIsHit() {
        // if hit then IsHit is True
        assertFalse("Ship is hit",ship.isHit());
    }

    @Test
    public void testTakeDamage() {
        ship.takeDamage(3);
        assertEquals("Health not decreased by correct amount", 5 , (int) ship.getHealth());
    }

    @Test
    public void testScaleDamage() {
        assertEquals("Is starting scale damage 1", 5, (int) ship.getDamageScaler());
        ship.scaleDamage(2);
        assertEquals("Is the scalled damaged scalled by the right amount", 10, (int) ship.getDamageScaler());
    }

    @Test
    public void testIsAlive() {
        assertTrue("Is ship alive", ship.isAlive());
        ship.takeDamage(100);
        assertFalse("Is ship dead", ship.isAlive());
    }

    @Test
    public void testUpdateState() {
        GameState newGameState = new GameState();
        float health = (float)ship.getHealth();
        System.out.println(ship.getHealth());
        ship.takeDamage(3);
        System.out.println(ship.getHealth());
        ship.updateState(newGameState);
        float random = 0f;
        float newhealth = health - (ship.getDamageScaler() * 3);
        //assert equal depricated unless three inputs of doubles
        assertEquals("Update state", (float)newhealth, (float)newGameState.shipHealths.get(0),random);
    }

    @Test
    public void testDispose() {
        ship.dispose();
    }
}
