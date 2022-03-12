package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Projectile extends Actor {

    // TODO add collision code


    private float speed;
    private static float lifespan;

    private Sprite sprite;
    private float lifespanTimer;

    // Box2d stuff
    private World world;

    private Body body;
    private static BodyDef bodyDef;
    private static FixtureDef fixtureDef;
    private static float radius;
    static {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        Shape shape = new CircleShape();
        shape.setRadius(radius);


        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.shape.setRadius(radius);
    }





    public Projectile(World world,Vector2 position, float angle) {


        body.setTransform(position, angle);
        float velX = MathUtils.cos(angle);
        float velY = MathUtils.sin(angle);
        body.setLinearVelocity(velX, velY);
    }


    @Override
    public void act(float delta) {
        super.act(delta);


        lifespanTimer += delta;
        if (lifespanTimer > lifespan) {
            // delete object
            world.destroyBody(body);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch);
    }
}
