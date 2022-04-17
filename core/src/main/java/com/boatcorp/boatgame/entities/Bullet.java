package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.boatcorp.boatgame.GameState;

/**
 * Creates a Bullet object
 */
public class Bullet extends Group {
    private final Sprite sprite;
    private final Vector2 startPos;
    private final Vector2 velocity;
    private final Body bodyd;
    private final World gameWorld;
    private final String firedFrom;
    private GameState state;
    private String color;

    /**
     * Initialises a bullet with a texture at the required position
     * @param position The position where the bullet should be drawn
     * @param velocity The velocity of the object which created it
     * @param world The world object which box2D objects are stored in
     */

    public Bullet(Vector2 position, Vector2 velocity, World world, String firedFrom, String color, GameState state) {
        final Texture texture = new Texture("Entities/" + color + ".png");
        sprite = new Sprite(texture);
        startPos = position.cpy();
        this.velocity = velocity;
        gameWorld = world;
        this.firedFrom = firedFrom;
        this.state = state;
        this.color = color;

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyd = gameWorld.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        bodyd.createFixture(fixtureDef).setUserData((firedFrom+"Bullet"));
        bodyd.setUserData("");
        shape.dispose();
        bodyd.setLinearVelocity(velocity);

        this.addActor(new Image(sprite));
        this.setPosition(position.x,position.y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);


    }


    @Override
    public void act(float delta) {
        super.act(delta);
        move(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));

    }

    public String getFiredFrom(){return firedFrom;}


    /**
     * Returns true if bullet has travelled more than the max range (300 units)
     * @param range the maximum distance the bullet can travel
     * @return a bool of if the bullet is out of range
     */
    public boolean outOfRange(int range) {
        double distance = Math.hypot((bodyd.getPosition().x- sprite.getWidth()/2) - startPos.x, (bodyd.getPosition().y- sprite.getHeight()/2) - startPos.y);
        return (distance > range);
    }

    /**
     * Returns if the bullet has hit another object
     * @return True if hit, false if not hit
     */
    public boolean hit(){
        return bodyd.getUserData() == "Hit";
    }

    /**
     * Updates the position of the bullet relative to its velocity
     */
    public void move(float delta) {
        bodyd.setLinearVelocity(velocity.x / delta, velocity.y / delta);
    }


    /**
     * Disposes of unwanted objects
     */
    public void dispose() {
        gameWorld.destroyBody(bodyd);
    }

    public Vector2 getVelocity(){
        return bodyd.getLinearVelocity();
    }

    public void updateState(GameState newState){
        newState.firedFroms.add(firedFrom);
        newState.velocities.add(bodyd.getLinearVelocity());
        newState.positions.add(bodyd.getPosition());
        newState.bulletColors.add(color);
    }
}




