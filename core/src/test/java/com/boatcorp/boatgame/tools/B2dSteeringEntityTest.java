package com.boatcorp.boatgame.tools;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.entities.Player;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class B2dSteeringEntityTest {

    public static B2dSteeringEntity entity;
    public static Application app;
    private static GameState state;
    private static Player player;
    private  SteeringBehavior<Vector2> behav;
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
        player = new Player(new World(new Vector2(0,0),true),state);
        entity = new B2dSteeringEntity(player.getBody(),2);

    }



    @Test
    public void getBody() {
        assertEquals("Check if function returns the correct body",player.getBody(), entity.getBody());
    }

    @Test
    public void behaviour(){
        behav = new SteeringBehavior<Vector2>(new Steerable<Vector2>() {
            @Override
            public Vector2 getLinearVelocity() {
                return null;
            }

            @Override
            public float getAngularVelocity() {
                return 0;
            }

            @Override
            public float getBoundingRadius() {
                return 0;
            }

            @Override
            public boolean isTagged() {
                return false;
            }

            @Override
            public void setTagged(boolean tagged) {

            }

            @Override
            public float getZeroLinearSpeedThreshold() {
                return 0;
            }

            @Override
            public void setZeroLinearSpeedThreshold(float value) {

            }

            @Override
            public float getMaxLinearSpeed() {
                return 0;
            }

            @Override
            public void setMaxLinearSpeed(float maxLinearSpeed) {

            }

            @Override
            public float getMaxLinearAcceleration() {
                return 0;
            }

            @Override
            public void setMaxLinearAcceleration(float maxLinearAcceleration) {

            }

            @Override
            public float getMaxAngularSpeed() {
                return 0;
            }

            @Override
            public void setMaxAngularSpeed(float maxAngularSpeed) {

            }

            @Override
            public float getMaxAngularAcceleration() {
                return 0;
            }

            @Override
            public void setMaxAngularAcceleration(float maxAngularAcceleration) {

            }

            @Override
            public Vector2 getPosition() {
                return null;
            }

            @Override
            public float getOrientation() {
                return 0;
            }

            @Override
            public void setOrientation(float orientation) {

            }

            @Override
            public float vectorToAngle(Vector2 vector) {
                return 0;
            }

            @Override
            public Vector2 angleToVector(Vector2 outVector, float angle) {
                return null;
            }

            @Override
            public Location<Vector2> newLocation() {
                return null;
            }
        }) {
            @Override
            protected SteeringAcceleration<Vector2> calculateRealSteering(SteeringAcceleration<Vector2> steering) {
                return null;
            }
        };

        entity.setBehavior(behav);
        assertEquals(behav,entity.getBehavior());

    }

    @Test
    public void update() {
        entity.setBehavior(behav);
        entity.update(0.016f);
        entity.getLinearVelocity();
        entity.getAngularVelocity();
        assertEquals("Is the bounding radius correct",2, entity.getBoundingRadius(),0);



    }

    @Test
    public void setTagged() {
        assertEquals(false,entity.isTagged());
        entity.setTagged(true);
        assertEquals(true,entity.isTagged());
    }


    @Test
    public void setMaxLinearSpeed() {
        entity.setMaxLinearSpeed(100f);
        assertEquals(100f,entity.getMaxLinearSpeed(),0);
    }

    @Test
    public void setMaxLinearAcceleration() {
        entity.setMaxLinearAcceleration(100f);
        assertEquals(100f,entity.getMaxLinearAcceleration(),0);
    }

    @Test
    public void setMaxAngularSpeed() {
        entity.setMaxAngularSpeed(100f);
        assertEquals(100f,entity.getMaxAngularSpeed(),0);
    }


    @Test
    public void setMaxAngularAcceleration() {
        entity.setMaxAngularAcceleration(100f);
        assertEquals(100f,entity.getMaxAngularAcceleration(),0);
    }

    @Test
    public void getPosition() {
        assertEquals(new Vector2(100,100), entity.getPosition());
    }


    @Test
    public void vectorToAngle() {
        Vector2 test = new Vector2(5,5);
        assertEquals(-45,entity.vectorToAngle(test)* 180 / Math.PI,0.001f);
    }

    @Test
    public void angleToVector() {
        assertEquals(-1, entity.angleToVector(new Vector2(0,0),(float)Math.PI).y,0);
    }

    @Test
    public void newLocation() {
        assertNull(entity.newLocation());
    }


    @Test
    public void setZeroLinearSpeedThreshold() {
        entity.setZeroLinearSpeedThreshold(5f);
        assertEquals("Check if speed threshold set correctly",5f,entity.getZeroLinearSpeedThreshold(),0);
    }

    @Test
    public void setOrientation() {
        entity.setOrientation(0f);
        assertEquals("Check if speed Orientation set correctly",0f,entity.getOrientation(),0);
    }
}