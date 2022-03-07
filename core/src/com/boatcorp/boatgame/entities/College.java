package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.frameworks.HealthBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Creates a collage object
 */
public class College {
    private final SpriteBatch batch;
    private final Sprite sprite;
    /**Relative position of the player*/
    private Vector2 position;
    /**list of bullets which are currently on screen*/
    private ArrayList<Bullet> bullets;
    /**Holds the vectors to fire the bullets diagonally*/
    private final ArrayList<Vector2> diagonalDirections;
    /**Holds the vectors to fire the bullets in cardinal directions*/
    private final ArrayList<Vector2> cardinalDirections;
    /**Holds the vectors to fire the bullets in a rotating patter*/
    private final ArrayList<Vector2> rotatingDirections;
    /**The attack patterns of the collages*/
    private final ArrayList<ArrayList<Vector2>> attackPatterns;
    private final HealthBar health;
    /**The collages max health*/
    private final float maxHealth;
    /**The collages current health*/
    private float currentHealth;
    private Body bodyd;
    private World gameWorld;
    private GameState state;
    private Object college;

    /**
     * Constructor class to create and initialise a new collage
     * @param college a String stating the name of the collage, used to get the image path
     */

    public College(Object college, World world, GameState state) {
        final String PATH_NAME = "Entities/" + college + ".png";
        final Texture texture = new Texture(Gdx.files.internal(PATH_NAME));
        batch = new SpriteBatch();
        sprite = new Sprite(texture);
        bullets = new ArrayList<>();
        Random rand = new Random();
        position = state.collegePositions.get(college);
        health = new HealthBar();
        maxHealth = state.collegeHealths.get(college)[1];
        currentHealth = state.collegeHealths.get(college)[0];
        gameWorld = world;
        this.state = state;
        this.college = college;

        cardinalDirections = new ArrayList<>();
        cardinalDirections.add(new Vector2(5,0));
        cardinalDirections.add(new Vector2(-5,0));
        cardinalDirections.add(new Vector2(0,5));
        cardinalDirections.add(new Vector2(0,-5));

        diagonalDirections = new ArrayList<>();
        diagonalDirections.add(new Vector2(4,4));
        diagonalDirections.add(new Vector2(4,4));
        diagonalDirections.add(new Vector2(4,-4));
        diagonalDirections.add(new Vector2(-4,4));
        diagonalDirections.add(new Vector2(-4,-4));

        rotatingDirections = new ArrayList<>();
        rotatingDirections.add(new Vector2(5, 0));
        rotatingDirections.add(new Vector2(4, 4));
        rotatingDirections.add(new Vector2(-5, 0));
        rotatingDirections.add(new Vector2(4, 4));
        rotatingDirections.add(new Vector2(4, -4));
        rotatingDirections.add(new Vector2(0, 5));
        rotatingDirections.add(new Vector2(-4, 4));
        rotatingDirections.add(new Vector2(-4, -4));
        rotatingDirections.add(new Vector2(0, -5));


        attackPatterns = new ArrayList<>();
        attackPatterns.add(cardinalDirections);
        attackPatterns.add(rotatingDirections);
        attackPatterns.add(diagonalDirections);

        //Creates body definition for collages
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);
        bodyd = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyd.createFixture(fixtureDef).setUserData("College");
        bodyd.setUserData(college);
        shape.dispose();

    }

    /**
     * Gets the current position of the player
     * @return A copy of it's position
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * Logic for calculating collisions and rendering bullets
     * @param camera The current camera being used render the bullets
     * @param player The player object
     * @param delta Time since last function call
     */
    public void combat(Matrix4 camera, Player player, float delta) {
        Vector2 playerPos = player.getPosition();
        double distance = Math.hypot((position.x+ (sprite.getWidth()/2)) - playerPos.x, (position.y+ (sprite.getHeight()/2)) - playerPos.y);
        Random rand = new Random();
        ArrayList<Vector2> randDir;

        ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
        // Only begins combat when the player is close enough and the college isn't defeated
        if (distance < 200) {
            if (bullets.isEmpty()) {
                // Randomly choose from set attack patterns
                int random_number = rand.nextInt(attackPatterns.size());
                randDir = attackPatterns.get(random_number);
                for (Vector2 direction : randDir) {
                    bullets.add(new Bullet(this.getPosition(), direction, gameWorld, "College"));
                }
            }
            for (Bullet bullet: bullets) {
                // Draw and move bullets and check for collisions
                bullet.setMatrix(camera);
                bullet.draws();
                bullet.move(delta);
                if (bullet.outOfRange(300)) {
                    bullet.dispose();
                    toRemove.add(bullet);
                }
                if (player.isHit() && bullet.hit()) {
                    bullet.dispose();
                    toRemove.add(bullet);
                    player.takeDamage(10);
                }
            }
            bullets.removeAll(toRemove);

        } else{
            if (!bullets.isEmpty()) {
                for (Bullet bullet: bullets){
                    if (bullet.outOfRange(300)) {
                        bullet.dispose();
                        toRemove.add(bullet);
                    }
                }
            }
            bullets.removeAll(toRemove);
        }

    }

    /**
     * Draws the collage in the randomly selected position
     */
    public void draw() {
        float correctPosX = position.x- (sprite.getWidth()/2);
        float correctPosY = position.y - (sprite.getHeight()/2) ;
        batch.begin();
        sprite.setPosition(correctPosX, correctPosY);
        sprite.draw(batch);
        batch.end();
        health.draw(new Vector2(correctPosX - 9.5f, correctPosY - 5), maxHealth, currentHealth, 0.5f);
    }

    /**
     * @return a float of the collages current health
     */
    public float getHealth() {
        return this.currentHealth;
    }

    /**
     * @return a boolean if the collage is alive or not
     */
    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    /**
     * Returns True if hit
     * @return a boolean if the collage has been hit
     */
    public boolean isHit(){
        if (bodyd.getUserData() == "Hit"){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Reduces the collages' health by a given amount
     * @param damage the damage inflicted onto the collage
     */
    public void takeDamage(int damage) {
        if (this.getHealth() > 0) {
            currentHealth -= damage;
        }
    }

    /**
     * Renders the health bar above the power
     * @param combined used to set the projection matrix to the correct amount inside the batch renderer
     */
    public void setMatrix(Matrix4 combined) {
        batch.setProjectionMatrix(combined);
        health.setMatrix(combined);
    }

    /**
     * Disposes of each of the unneeded objects
     */
    public void dispose() {
        batch.dispose();
        health.dispose();
        gameWorld.destroyBody(bodyd);
        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                bullet.dispose();
            }
        }
    }

    public Object getUserData(){
        return college;
    }
    public float getMaxHealth(){
        return maxHealth;
    }
    public float getCurrentHealth(){
        return currentHealth;
    }
    public void updateState(){
        float[] healths = {currentHealth,maxHealth};
        state.collegeHealths.put(college, healths);
        state.collegePositions.put(college, this.getPosition());
    }

}

