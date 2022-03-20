package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.boatcorp.boatgame.frameworks.HealthBar;
import com.boatcorp.boatgame.frameworks.PlunderSystem;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.GameState;
import java.util.ArrayList;

/**
 * Creates a Player object
 */
public class Player extends Group {
    private final Sprite sprite;
    private final HealthBar health;
    private final float maxHealth;
    private float currentHealth;
    private int immuneSeconds;
    private float timeSeconds;
    private final float period;
    private String bulletColor;

    private float damageScaler;
    private final ArrayList<Bullet> bullets;
    private long timeSinceLastShot;
    private final Body bodyd;
    private final GameState state;
    private final World gameWorld;


    private static final float PLAYER_SPEED = 100f;

    private int BULLET_SPEED = 20;

    private Vector2 velocity;


    /**
     * Initialises a Player with a texture at the required position, along with other relevant attributes
     * @param world the current world which contains the box2D objects
     * @param state the gamestate which holds the parameters needed to save/reload the game
     */
    public Player(World world, GameState state) {
        velocity = new Vector2(0,0);
        Texture texture = new Texture(Gdx.files.internal("Entities/boat1.png"));
        sprite = new Sprite(texture);
        health = new HealthBar();
        bullets = new ArrayList<>();
        maxHealth = state.maxHealth;
        currentHealth = state.currentHealth;
        gameWorld = world;
        timeSinceLastShot = TimeUtils.millis();
        immuneSeconds = state.immuneSeconds;
        timeSeconds = 0f;
        period = 1f;
        damageScaler = state.damageScaler;
        this.state = state;
        bulletColor = state.bulletColor;

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(state.playerPosition.x,state.playerPosition.y);
        bodyDef.fixedRotation = true;
        bodyd = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        bodyd.createFixture(fixtureDef).setUserData("Player");
        bodyd.setUserData("");

        shape.dispose();

        //Creates sprite
        this.addActor(new Image(sprite));
        this.setPosition(100,100);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    /**
     * Gets the position of the players position
     * @return a Vector2 of a copy of the players current position
     */
    public Vector2 getPosition() {
        return bodyd.getPosition();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));

    }

    /**
     * Updates the position,rotation and velocity of the player
     * @param delta time since function last called
     */
    public void update (float delta) {

        // Process player movement
        movement(delta);

        // Rotate sprite to match movement
        if (bodyd.getLinearVelocity().x != 0 || bodyd.getLinearVelocity().y != 0){
            this.setRotation(velocity.angleDeg() - 90);
            bodyd.setTransform(bodyd.getPosition(),velocity.angleRad() - ((float) Math.PI/2));
        }
        //counts down immunity
        if (immuneSeconds > 0){
            timeSeconds += Gdx.graphics.getDeltaTime();
            if(timeSeconds > period){
                timeSeconds -= period;
                immuneSeconds -= period;
                //TODO: display to screen
            }
        }


    }

    public void setBulletColor(String color){bulletColor = color;}

    public String getBulletColor(){ return bulletColor;}

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

    public void setHealth(float newHealth){currentHealth = newHealth;}


    public int getImmuneSeconds(){return immuneSeconds;}

    public void setImmuneSeconds(int newTime){immuneSeconds = newTime;}

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
        if (this.getHealth() > 0 && immuneSeconds == 0) {
            currentHealth -= damage*damageScaler;
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
     * Returns True if hit
     * @return a boolean if the player has been hit
     */
    public boolean isHit(){
        return bodyd.getUserData() == "Hit";
    }

    /**
     * Logic for calculating bullet position
     * @return a list of bullets
     */
    public ArrayList<Bullet> combat(ArrayList<College> colleges,ArrayList<EnemyShip> enemyShips,ArrayList<SeaMonster> seaMonsters) {

        boolean up = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT);

        if (up || down || left ||right) {
            if (((TimeUtils.timeSinceMillis(timeSinceLastShot)) > 250)) {
                timeSinceLastShot = TimeUtils.millis();
                Vector2 inputVector = new Vector2(0, 0);

                // Handle player input
                if (up && !down) {
                    // Shoot up.
                    inputVector.y = 1;
                } else if (down && !up){
                    // Shoot down.
                    inputVector.y = -1;
                }

                if (right && !left) {
                    // Shoot right.
                    inputVector.x = 1;
                } else if (left && !right) {
                    // Shoot left.
                    inputVector.x = -1;
                }

                // Normalise inputVector
                inputVector.nor();

                float velX = inputVector.x * BULLET_SPEED;
                float velY = inputVector.y * BULLET_SPEED;

                Vector2 bulletVelocity = new Vector2(velX, velY);

                // Sets bullet velocity to current velocity of boat x2, ensuring no division by zero errors
                bullets.add(new Bullet(bodyd.getPosition(), bulletVelocity, gameWorld, "Player", bulletColor));
            }
        }
        ArrayList<Bullet> toRemove = new ArrayList<>();
        if (!bullets.isEmpty()){
            for (Bullet bullet: bullets) {
                // Draw and move bullets and check for collisions
                if (bullet.outOfRange(200)) {
                    bullet.dispose();
                    toRemove.add(bullet);
                }
                for (College college : colleges) {
                    if (college.isHit() && bullet.hit()) {
                        bullet.dispose();
                        toRemove.add(bullet);
                        college.takeDamage(5);
                    }
                }
                for (EnemyShip ship : enemyShips){
                    if (ship.isHit() && bullet.hit()){
                        bullet.dispose();
                        toRemove.add(bullet);
                        ship.takeDamage(5);
                    }
                }
                for (SeaMonster monster : seaMonsters){
                    if (monster.isHit() && bullet.hit()){
                        bullet.dispose();
                        toRemove.add(bullet);
                        //If green upgrade has been bought then it increases damage to sea monster
                        if (bulletColor.equals("greenbullet")){
                            monster.takeDamage(10);
                        }else{
                            monster.takeDamage(5);
                        }

                    }
                }
            }
        }
        bullets.removeAll(toRemove);
        return bullets;
    }

    /**
     * Disposes of each of the unneeded objects
     */
    public void dispose() {
        health.dispose();
        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                bullet.dispose();
            }
        }
    }


    public void scaleDamage(float scale){
        damageScaler *= scale;

    }

    /**
     * Gets the body of the player
     * @return a box2D object of the player
     */
    public Body getBody(){
        return bodyd;
    }
    
    /**
     * Updates the gamestate with the player's properties
     */
    public void updateState(){
        state.playerPosition = this.getPosition();
        state.currentHealth = this.getHealth();
        state.maxHealth = this.getMaxHealth();
        state.points = PointSystem.getPoints();
        state.plunder = PlunderSystem.getPlunder();
        state.immuneSeconds = immuneSeconds;
        state.damageScaler = damageScaler;
        state.bulletColor = bulletColor;
    }

}

