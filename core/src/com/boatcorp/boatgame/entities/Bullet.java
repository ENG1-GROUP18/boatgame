package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.jetbrains.annotations.NotNull;

import static com.boatcorp.boatgame.screens.Constants.BULLET_PATH;

/**
 * Creates a Bullet object
 */
public class Bullet {
    private final SpriteBatch batch;
    private final Sprite sprite;
    private Vector2 position;
    private final Vector2 startPos;
    private final Vector2 velocity;
    private Body bodyd;
    private World gameWorld;

    /**
     * Initialises a bullet with a texture at the required position
     * @param position The position where the bullet should be drawn
     * @param velocity The velocity of the object which created it
     */
    public Bullet(Vector2 position, Vector2 velocity, World world) {
        final Texture texture = new Texture(Gdx.files.internal(BULLET_PATH));
        batch = new SpriteBatch();
        sprite = new Sprite(texture);
        this.position = position;
        startPos = getPosition();
        this.velocity = velocity;
        gameWorld = world;

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);
        bodyd = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyd.createFixture(shape, 0.0f);
        shape.dispose();

    }

    /**
     * Draws the updated position of the bullet
     */
    public void draw() {
        sprite.setPosition(bodyd.getPosition().x - sprite.getWidth()/2,bodyd.getPosition().y-(sprite.getHeight())/2);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    /**
     * Gets the position of the bullet
     * @return a Vector2 of a copy of the bullets current position
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * sets the position of the object to the inputted vector
     * @param pos the position where the bullet should be drawn at
     */
    public void setPosition(@NotNull Vector2 pos) {
        position.x = pos.x;
        position.y = pos.y;
    }

    /**
     * Gets the bullets current velocity
     * @return a Vector2 of a copy of the bullets current velocity
     */
    public Vector2 getVelocity() {
        return velocity.cpy();
    }

    /**
     * Returns true if bullet has travelled more than the max range (300 units)
     * @param range the maximum distance the bullet can travel
     * @return a bool of if the bullet is out of range
     */
    public boolean outOfRange(int range) {
        double distance = Math.hypot(position.x - startPos.x, position.y - startPos.y);
        return (distance > range);
    }

    /**
     * Calculates weather a collision has occurred
     * @param position the position of the target
     * @return True if the bullet collides with target
     */
    public boolean hitTarget(@NotNull Vector2 position) {
        position.add(10,10); // Centre hitbox of bullet
        Vector2 currentPos = this.getPosition();

        double distance = Math.hypot(currentPos.x - position.x, currentPos.y - position.y);
        return (distance < 16);
    }

    /**
     * Updates the position of the bullet relative to its velocity
     */
    public void move(float delta) {
        bodyd.setLinearVelocity(velocity.x / delta, velocity.y / delta);
        Vector2 currentPos = this.getPosition();
        Vector2 v = this.getVelocity();
        currentPos.x += v.x;
        currentPos.y += v.y;
        this.setPosition(currentPos);
    }

    /**
     * Sets the correct batch projecting matrix
     * @param combined used to set the projection matrix to the correct amount inside the batch renderer
     */
    public void setMatrix(Matrix4 combined) {
        batch.setProjectionMatrix(combined);
    }

    /**
     * Disposes of unwanted objects
     */
    public void dispose() {
        gameWorld.destroyBody(bodyd);
        batch.dispose();
    }
}
