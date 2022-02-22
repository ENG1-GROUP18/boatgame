package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.HealthBar;
import com.boatcorp.boatgame.frameworks.PointSystem;

import java.util.ArrayList;

/**
 * Creates a Player object
 */
public class Player {
    private final SpriteBatch batch;
    private final Texture texture = new Texture(Gdx.files.internal("Entities/boat1.png"));
    private final Sprite sprite;
    private final HealthBar health;
    private final float maxHealth;
    private float currentHealth;
    private final ArrayList<Bullet> bullets;
    private final Viewport viewport;
    private long timeSinceLastShot;
    private Body bodyd;

    private final World gameWorld;


    private static final float PLAYER_SPEED = 100f;

    private int BULLET_SPEED = 2;
    
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 inputVector;


    /**
     * Initialises a Player with a texture at the required position, along with other relevant attributes
     * @param view the current viewport
     */
    public Player(Viewport view, World world) {
        position = new Vector2(100,100);
        velocity = new Vector2(0,0);
        batch = new SpriteBatch();
        sprite = new Sprite(texture);
        health = new HealthBar();
        bullets = new ArrayList<>();
        maxHealth = 100;
        currentHealth = 100;
        viewport = view;
        gameWorld = world;
        timeSinceLastShot = TimeUtils.millis();

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(100,100);
        bodyd = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyd.createFixture(shape, 0.0f);
        shape.dispose();
    }

    /**
     * Gets the position of the players position
     * @return a Vector2 of a copy of the players current position
     */
    public Vector2 getPosition() {
        return position.cpy();
    }

    /**
     * Draws the player in its updated position
     */
    public void draw() {
        sprite.setPosition(position.x-(sprite.getWidth()/2), position.y-(sprite.getHeight()/2));
        batch.begin();
        sprite.draw(batch);
        batch.end();

    }


    /**
     * Updates the position,rotation and velocity of the player
     * @param delta time since function last called
     */
    public void update (float delta) {

        // Process player movement
        movement(delta);

        // Rotate sprite to match movement
        sprite.setRotation(velocity.angleDeg() - 90);


        Vector2 prev_position = position.cpy();
        //TODO remove when box2d hitbox is implemented
        position.x = MathUtils.clamp(position.x + velocity.x, 0, 1421);
        position.y = MathUtils.clamp(position.y + velocity.y, 0, 1371);

        if (prev_position.x == position.x && prev_position.y == position.y){
            velocity.x = 0;
            velocity.y = 0;
        }

    }

    /**
     * Updates the velocity of the player dependent on user directional inputs
     * @param delta time since function last called
     */
    private void movement(final float delta) {
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);

        Vector2 inputVector = new Vector2(0, 0);

        // Handle player input
        if (up && !down) {
            // Move up.
            inputVector.y = 1;
        } else if (down && !up){
            // Move down.
            inputVector.y = -1;
        }

        if (right && !left) {
            // Move right.
            inputVector.x = 1;
        } else if (left && !right) {
            // Move left.
            inputVector.x = -1;
        }

        // Normalise inputVector
        inputVector.nor();

        // Update player velocity
        velocity.x = inputVector.x * PLAYER_SPEED * delta;
        velocity.y = inputVector.y * PLAYER_SPEED * delta;

        bodyd.setLinearVelocity(velocity.x/delta, velocity.y/delta);
    }

    /**
     * Returns the players current health
     * @return a float of the player health
     */
    public float getHealth() {
        return currentHealth;
    }

    /**
     * Returns the width and height of the player sprite
     * @return a Vector2 of the dimensions of the sprite
     */
    public Vector2 getSpriteDimensions(){return new Vector2((sprite.getHeight()),(sprite.getWidth()));}

    /**
     * Returns the players maximum health
     * @return a float of max health
     */
    public float getMaxHealth() { return maxHealth; }

    /**
     * Reduces the players' health by a given amount
     * @param damage the damage inflicted onto the player
     */
    public void takeDamage(int damage) {
        if (this.getHealth() > 0) {
            currentHealth -= damage;
        }
    }

    /**
     * Determines if the player is dead
     * @return True if currentHealth is zero
     */
    public boolean isDead() {
        return currentHealth <= 0;
    }

    /**
     * Logic for calculating bullet position and rendering bullets
     * @param camera The current camera being used render the bullets
     * @param colleges The colleges currently alive on the map
     */
    public void combat(Matrix4 camera, ArrayList<College> colleges) {
        if (Gdx.input.isTouched() || !bullets.isEmpty()) {
            if (bullets.isEmpty() && ((TimeUtils.timeSinceMillis(timeSinceLastShot)) > 500)) {
                timeSinceLastShot = TimeUtils.millis();
                Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                Vector3 newPosition = viewport.unproject(mousePosition.cpy());
                float velX = newPosition.x - position.x;
                float velY = newPosition.y - position.y;
                float length = (float) Math.sqrt(velX * velX + velY * velY);
                if (length != 0) {
                    velX = velX * BULLET_SPEED / length;
                    velY = velY * BULLET_SPEED / length;
                }
                Vector2 adjustedPos = this.getPosition().add(0,0); // unadjusted position
                Vector2 bulletVelocity = new Vector2(velX, velY);

                // Sets bullet velocity to current velocity of boat x2, ensuring no division by zero errors
                bullets.add(new Bullet(adjustedPos, bulletVelocity));
            }
            for (int i = 0; i < bullets.size(); i++) {
                // Draw and move bullets and check for collisions
                Bullet bullet = bullets.get(i);
                bullet.setMatrix(camera);
                bullet.draw();
                bullet.move();
                if (bullet.outOfRange(200)) { bullets.remove(bullet); }
                for (College college : colleges) {
                    if (bullet.hitTarget(college.getPosition())) {
                        bullets.remove(bullet);
                        college.takeDamage(5);
                    }
                }
            }
        }
    }

    /**
     * Sets the correct batch projecting matrix
     * @param combined used to set the projection matrix to the correct amount inside the batch renderer
     */
    public void setMatrix(Matrix4 combined) {
        batch.setProjectionMatrix(combined);
    }

    /**
     * Disposes of each of the unneeded objects
     */
    public void dispose() {
        batch.dispose();
        health.dispose();
        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                bullet.dispose();
            }
        }
    }
}
