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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.HealthBar;
import com.boatcorp.boatgame.frameworks.PointSystem;

import java.util.ArrayList;

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

    private static final float PLAYER_SPEED = 100f;

    private int BULLET_SPEED = 2;
    
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 inputVector;



    public Player(Viewport view) {
        position = new Vector2(100,100);
        velocity = new Vector2(0,0);
        batch = new SpriteBatch();
        sprite = new Sprite(texture);
        health = new HealthBar();
        bullets = new ArrayList<>();
        maxHealth = 100;
        currentHealth = 100;
        viewport = view;
        timeSinceLastShot = TimeUtils.millis();
    }

    public Vector2 getPosition() {
        return position.cpy();
    }

    public void draw() {
        sprite.setPosition(position.x-(sprite.getWidth()/2), position.y-(sprite.getHeight()/2));

        batch.begin();
        sprite.draw(batch);
        batch.end();

    }

    public void update (float delta) {
        // Process player movement
        movement(delta);

        // Rotate sprite to match movement
        sprite.setRotation(velocity.angleDeg() - 90);


        Vector2 prev_position = position.cpy();
        //TODO remove when box2d hibox is implemented
        position.x = MathUtils.clamp(position.x + velocity.x, 0, 1421);
        position.y = MathUtils.clamp(position.y + velocity.y, 0, 1371);

        if (prev_position.x == position.x && prev_position.y == position.y){
            velocity.x = 0;
            velocity.y = 0;
        }

    }

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
    }

    public float getHealth() {
        return currentHealth;
    }

    public Vector2 getSpriteDimensions(){return new Vector2((sprite.getHeight()),(sprite.getWidth()));}

    public float getMaxHealth() { return maxHealth; }

    public void takeDamage(int damage) {
        if (this.getHealth() > 0) {
            currentHealth -= damage;
        }
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

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

    public void setMatrix(Matrix4 combined) {
        batch.setProjectionMatrix(combined);
    }

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
