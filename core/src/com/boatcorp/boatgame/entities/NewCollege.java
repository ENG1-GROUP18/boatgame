package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.geom.Point2D;
import java.util.*;

public class NewCollege extends Actor {

    //TODO notes:
    // look into using JSON to store info about each college, have loader that creates colleges
    // LOOK INTO EVENTS, LISTENERS etc etc

    private Vector2 position;
    private boolean inCombat;

    private Sprite sprite;
    private World world;
    private Body body;

    private ArrayList<attackPattern> attackPatternPool = new ArrayList<>();


    public NewCollege(World world, float posX, float posY) {
        this.world = world;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("Entities/james.png")));
        this.position = new Vector2(posX, posY);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // TODO check if player in range


        if (inCombat) {
            attack();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch);
    }

    private void attack() {
        // Move on to next attack pattern?


    }

    private void addAttackPattern() {

    }

    private class attackPattern {

        boolean aimAtPlayer;
        float interval;
        int[] angles;

        private void addAttackPattern(float interval, int[] angles) {


        }

        private void attack() {
            for (int i = 0 ; i < angles.length; i++) {
                new Projectile(world, position, angles[i]);
            }

        }
    }

    private class patternPool {

        ArrayList<attackPattern> attackPatternPool= new ArrayList<>();
        Iterator<attackPattern> it = attackPatternPool.iterator();
        private int counter;



        private void patternPool(float interval, int[] angles) {


        }

        private void addPattern() {

        }

        private void attack() {

        }

    }


}


