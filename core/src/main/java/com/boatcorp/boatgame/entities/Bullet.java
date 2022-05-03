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
 * Creates a Bullet object which contains relevant information such as speed, position and colour.
 * In assessment 2 this class has been changed by implementing box2D into the bullet, so it is now a body.
 * This was done to improve collision detection.
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

    /**
     * Moves the bullet the required distance on screen
     * @param delta The current frame time
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        move(delta);
    }

    /**
     * Draws the bullet object at the required position
     * @param batch a Batch object which is used to collate all the renewable objects in each render cycle
     * @param parentAlpha the opacity of the object
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));

    }

    /**
     * Gets the object the bullet was fired from
     * @return a string representing which object the bullet was fired from
     */
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

    /**
     * Gets the velocity of the bullet
     * @return the linear velocity of the bullet body as a Vector2
     */
    public Vector2 getVelocity(){
        return bodyd.getLinearVelocity();
    }

    /**
     * Updates the information about this bullet into a new state
     * @param newState a GameState object to hold the updated info about the Bullet
     */
    public void updateState(GameState newState){
        newState.firedFroms.add(firedFrom);
        newState.velocities.add(bodyd.getLinearVelocity());
        newState.positions.add(bodyd.getPosition());
        newState.bulletColors.add(color);
    }
}




