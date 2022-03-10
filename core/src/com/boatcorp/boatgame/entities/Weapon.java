package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Weapon extends Actor {

    float attackTimer;
    float attackRate = 2f;

    public Weapon() {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        attackTimer += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void attack() {
        System.out.println("pew pew");
        if (attackTimer > attackRate) {
            attackTimer = 0f;
        }
    }
}
