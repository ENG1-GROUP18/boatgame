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

public class SeaMonsterTest {
    public static SeaMonster monster;
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

        state.monsterStartPositions.add(new Vector2(250, 250));
        state.monsterHealths.add(20f);
        state.monsterPositions.add(new Vector2(250, 250));

        World world = new World(new Vector2(0, 0), true);

        monster = new SeaMonster(world, new Player(world, state),state, 0);

    }

    @Test
    public void testIsHit() {
        assertFalse("Ship is hit",monster.isHit());
    }

    @Test
    public void testTakeDamage() {
        monster.takeDamage(3);
        assertEquals("Health not decreased by correct amount", 17 , (int) monster.getHealth());
    }

    @Test
    public void testIsAlive() {
        assertTrue("Is ship alive", monster.isAlive());
        monster.takeDamage(100);
        assertFalse("Is ship dead", monster.isAlive());
    }

    @Test
    public void testDispose() {
        monster.dispose();
    }

    @Test
    public void testFreeze() {
        monster.freeze();
        boolean freeze_stat = monster.get_freeze();
        assertEquals("Enemy ship not frozen",true, freeze_stat);
    }

    @Test
    public void testUnfreeze() {
        monster.unfreeze();
        boolean freeze_stat = monster.get_freeze();
        assertEquals("Enemy ship not unfrozen",false, freeze_stat);
    }

    @Test
    public void testGetId() {
    }

    @Test
    public void testUpdateState() {
        GameState newGameState = new GameState();
        float health = (float)monster.getHealth();
        System.out.println(monster.getHealth());
        monster.takeDamage(3);
        System.out.println(monster.getHealth());
        monster.updateState(newGameState);
        float random = 0f;
        assertEquals("Update state", health-3, (float)newGameState.monsterHealths.get(0),random);
    }
}