package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Projectile extends Actor {

    // units moved per second

    private float speed;

    // TODO Do we even need??? can just get from box2d
    private Vector2 position;
    private float rotation;

    private static BodyDef bodydef = new BodyDef();
    private Body body;

    private static float lifespan;
    private float lifespanTimer;

    private Sprite sprite;

    public Projectile(Vector2 position, float angle) {
        // TODO create new box2d Body

        body.setTransform(position, angle);
        float velX = MathUtils.cos(angle);
        float velY = MathUtils.sin(angle);
        body.setLinearVelocity(velX, velY);
    }


    @Override
    public void act(float delta) {
        super.act(delta);

        // do we even need this? probably not
        position = body.getPosition();

        lifespan += delta;
        if (lifespanTimer > lifespan) {
            // delete object
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch);
    }
}
