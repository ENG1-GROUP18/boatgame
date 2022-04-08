package com.boatcorp.boatgame.tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class ShapeMaker {

    public static final float PPM = 25f;
    private ShapeMaker() {}

    public static Body createRectangle(final Vector2 position, final Vector2 size, final BodyDef.BodyType type, final World world, float density) {

        // define body
        final BodyDef bDef = new BodyDef();
        bDef.position.set(position.x / PPM, position.y / PPM);
        bDef.type = type;
        final Body body = world.createBody(bDef);

        // define shape
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / PPM, size.y / PPM);
        final FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = density;

        body.createFixture(fDef);
        shape.dispose();

        return body;

    }
}
