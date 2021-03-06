package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.frameworks.HealthBar;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Random;

/**
 * Creates a college object which holds relevant information such as position, health and collision data.
 * Changed in assessment 2 by adding box2D to the collages to improve collision detection.
 */
public class College {
    private final SpriteBatch batch;
    private final Sprite sprite;
    /**Relative position of the player*/
    private final Vector2 position;
    /**Holds the vectors to fire the bullets diagonally*/
    private final ArrayList<Vector2> diagonalDirections;
    /**Holds the vectors to fire the bullets in cardinal directions*/
    private final ArrayList<Vector2> cardinalDirections;
    /**Holds the vectors to fire the bullets in a rotating patter*/
    private final ArrayList<Vector2> rotatingDirections;
    /**The attack patterns of the colleges*/
    private final ArrayList<ArrayList<Vector2>> attackPatterns;
    private HealthBar health;
    /**The collages max health*/
    private final float maxHealth;
    /**The collages current health*/
    private float currentHealth;
    private final Body bodyd;
    private final World gameWorld;
    private final GameState state;
    private final Object college;
    private float damageScaler;
    private long timeSinceLastShot;

    /**
     * Constructor class to create and initialise a new college
     * @param college a String stating the name of the college, used to get the image path
     */

    public College(Object college, World world, GameState state) {
        final String PATH_NAME = "Entities/" + college + ".png";
        final Texture texture = new Texture(Gdx.files.internal(PATH_NAME));
        if (state.headless){ batch = Mockito.mock(SpriteBatch.class);}  else { batch = new SpriteBatch();}

        sprite = new Sprite(texture);
        position = state.collegePositions.get(college);
        if (!state.headless){
            health = new HealthBar();
        }
        maxHealth = state.collegeHealths.get(college)[1];
        currentHealth = state.collegeHealths.get(college)[0];
        damageScaler = 1f;
        gameWorld = world;
        this.state = state;
        this.college = college;
        timeSinceLastShot = TimeUtils.millis();
        if (!state.isSpawn){timeSinceLastShot = TimeUtils.millis() - state.collegeTimes.get(college);}

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
     * @param player The player object
     */
    public ArrayList<Bullet> combat(Player player) {
        Vector2 playerPos = player.getPosition();
        double distance = Math.hypot((position.x+ (sprite.getWidth()/2)) - playerPos.x, (position.y+ (sprite.getHeight()/2)) - playerPos.y);
        Random rand = new Random();
        ArrayList<Vector2> randDir;
        ArrayList<Bullet> bullets = new ArrayList<>();
        // Only begins combat when the player is close enough and the college isn't defeated
        if (distance < 200) {
            if ((TimeUtils.timeSinceMillis(timeSinceLastShot)) > 1000) {
                timeSinceLastShot = TimeUtils.millis();
                // Randomly choose from set attack patterns
                int random_number = rand.nextInt(attackPatterns.size());
                randDir = attackPatterns.get(random_number);
                for (Vector2 direction : randDir) {
                    bullets.add(new Bullet(this.getPosition(), direction, gameWorld, "College", "bullet", state));
                }
            }}
            return bullets;
    }

    /**
     * Draws the college in the randomly selected position
     */
    public void draw() {
        float correctPosX = position.x- (sprite.getWidth()/2);
        float correctPosY = position.y - (sprite.getHeight()/2) ;
        batch.begin();
        sprite.setPosition(correctPosX, correctPosY);
        sprite.draw(batch);
        batch.end();
        if (!state.headless) {
            health.draw(new Vector2(correctPosX - 9.5f, correctPosY - 5), maxHealth, currentHealth, 0.5f);
        }
    }

    /**
     * @return a float of the colleges current health
     */
    public float getHealth() {
        return this.currentHealth;
    }

    /**
     * @return a boolean if the college is alive or not
     */
    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    /**
     * Returns True if hit
     * @return a boolean if the college has been hit
     */
    public boolean isHit(){
        return bodyd.getUserData() == "Hit";
    }

    /**
     * Reduces the colleges' health by a given amount
     * @param damage the damage inflicted onto the collage
     */
    public void takeDamage(int damage) {
        if (this.getHealth() > 0) {
            currentHealth -= damage*damageScaler;
        }
    }

    /**
     * Renders the health bar above the power
     * @param combined used to set the projection matrix to the correct amount inside the batch renderer
     */
    public void setMatrix(Matrix4 combined) {
        batch.setProjectionMatrix(combined);
        if (!state.headless) {
            health.setMatrix(combined);
        }
    }

    /**
     * Disposes of each of the unneeded objects
     */
    public void dispose() {
        batch.dispose();
        if (!state.headless) {
            health.dispose();
        }
        gameWorld.destroyBody(bodyd);
    }
    /**
     * Gets the name of the college
     */
    public Object getUserData(){
        return college;
    }

    /**
     * Updates the game state with the college's properties
     * @param newState
     */
    public void updateState(GameState newState){
        float[] healths = {currentHealth,maxHealth};
        newState.collegeNames.add(college.toString());
        newState.collegeHealths.put(college, healths);
        newState.collegePositions.put(college, this.getPosition());
        newState.collegeTimes.put(college, TimeUtils.timeSinceMillis(timeSinceLastShot));
    }
    public void scaleDamage(float scale){
        damageScaler *= scale;
    }
    //Used for testing
    public float getDamageScaler (){
        return damageScaler;
    }

}




