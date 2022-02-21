package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Projectile {

    private SpriteBatch batch;

    public Projectile(SpriteBatch batch) {
        this.batch = batch;
    }

    private void update() {

    }

    private void render(Batch batch) {
        batch.begin();
        batch.end();
    }
}