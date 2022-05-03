package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.tools.B2dSteeringEntity;

import java.util.ArrayList;

/**
 * A class which creates enemy ships which follow the player and try to kill them. And it holds relevant data about
 * them such as position, damage scalar and if it is frozen.
 * This was added in assessment 2 to fulfil the requirement of combat enemy ships - USR23.
 */
public class EnemyShip extends Group {
    private final Sprite sprite;
    private float health;
    private final Body body;
    private final Body startBody;
    private final World gameWorld;
    private final int id;
    private final B2dSteeringEntity entity,targetPlayer,targetHome;
    private final Arrive<Vector2> arriveToPlayer,arriveToStartPos;
    private final Player player;
    private FiniteState currentState;
    private long timeSinceLastShot;
    private float damageScaler;
    private boolean isFrozen;
    private GameState state;


    public EnemyShip(World world, GameState state, Player player, int id){
        Texture texture = new Texture(Gdx.files.internal("Entities/boat2.png"));
        sprite = new Sprite(texture);
        gameWorld = world;
        this.player = player;
        timeSinceLastShot = TimeUtils.millis();
        damageScaler = state.shipDamageScaler;
        isFrozen = state.isFrozen;
        this.state = state;
        this.health = state.shipHealths.get(id);
        this.id = id;


        //Creates body definition for ship
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(state.shipPositions.get(id));
        bodyDef.fixedRotation = true;
        body = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef).setUserData("EnemyShip");
        body.setUserData("");
        body.setLinearDamping(3);

        //Creates body of return location
        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set(state.shipStartPositions.get(id));
        startBody = gameWorld.createBody(bodyDef1);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape;
        fixtureDef1.isSensor = true;
        startBody.createFixture(fixtureDef1);


        //Ship AI behaviour
        entity = new B2dSteeringEntity(body,10);
        targetPlayer = new B2dSteeringEntity(player.getBody(),10);
        targetHome = new B2dSteeringEntity(startBody,10);

        currentState = FiniteState.STAY;
        arriveToPlayer = new Arrive<>(entity,targetPlayer)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(50)
                .setDecelerationRadius(10);

        entity.setBehavior(arriveToPlayer);

        arriveToStartPos = new Arrive<>(entity,targetHome)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0)
                .setDecelerationRadius(10);


        this.addActor(new Image(sprite));
        this.setPosition(body.getPosition().x,body.getPosition().y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float dist = (float) Math.hypot(targetPlayer.getBody().getPosition().y-entity.getBody().getPosition().y,
                targetPlayer.getBody().getPosition().x-entity.getBody().getPosition().x);

        float distanceFromHome = (float) Math.hypot(startBody.getPosition().y-entity.getBody().getPosition().y,
                startBody.getPosition().x-entity.getBody().getPosition().x);

        //Logic on what current state the enemy ship is in
        if (dist < 150 && distanceFromHome < 300 && currentState != FiniteState.RETURN && !isFrozen){
            entity.setBehavior(arriveToPlayer); //TODO change so only sets on state change, could cause lag otherwise
            currentState = FiniteState.FOLLOW;
            entity.update(delta);
        } else if (!(entity.getLinearVelocity().isZero(0.01f)) && !isFrozen){
            currentState = FiniteState.RETURN;
            entity.setBehavior(arriveToStartPos);
            entity.update(delta);

        } else{
            currentState = FiniteState.STAY;
            entity.getBody().setLinearVelocity(0,0);
            entity.getBody().setAngularVelocity(0);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(body.getPosition().x-(sprite.getWidth()/2), body.getPosition().y-(sprite.getHeight()/2));
        this.setRotation((float)Math.toDegrees(body.getAngle()));
        super.draw(batch, parentAlpha);
    }

    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (currentState == FiniteState.FOLLOW) {
            // Only begins combat when the player is close enough and the college isn't defeated

            if ((TimeUtils.timeSinceMillis(timeSinceLastShot)) > 1000) {
                timeSinceLastShot = TimeUtils.millis();

                Vector2 velocity = new Vector2();
                velocity.x = (float) -Math.sin(entity.getOrientation());
                velocity.y = (float) Math.cos(entity.getOrientation());
                bullets.add(new Bullet(body.getPosition(), velocity, gameWorld, "EnemyShip", "bullet", state));

            }

        }
        return bullets;
    }

    private enum FiniteState {
        FOLLOW,
        RETURN,
        STAY
    }
    public void freeze(){isFrozen = true;}

    public void unfreeze(){isFrozen = false;}

    public boolean get_freeze(){return isFrozen;}

    public boolean isHit(){
        return body.getUserData() == "Hit";
    }

    public void takeDamage(int damage){
        if(health > 0){
            health -= (damage*damageScaler);
        }
    }

    public void scaleDamage(float scale){
        damageScaler *= scale;

    }

    public float getDamageScaler() {
        return damageScaler;
    }

    public float getHealth(){return health;}

    public boolean isAlive() {
        return health > 0;
    }

    public void updateState(GameState newState){
        newState.shipDamageScaler = damageScaler;
        newState.shipHealths.add(health);
        newState.shipPositions.add(body.getPosition());
        newState.shipStartPositions.add(startBody.getPosition());
        newState.shipTimes.add(TimeUtils.timeSinceMillis(timeSinceLastShot));
    }

    public void dispose() {
        this.setPosition(-100,-100);
        gameWorld.destroyBody(body);
        gameWorld.destroyBody(startBody);
    }
}

