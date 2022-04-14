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
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.tools.B2dSteeringEntity;

public class SeaMonster extends Group {
    Sprite sprite;
    private final Player player;
    private final Body body;
    private final Body startBody;
    private float health;
    private final World gameWorld;
    private FiniteState currentState;
    private final B2dSteeringEntity entity,targetPlayer,targetHome;
    private final Arrive<Vector2> arriveToPlayer,arriveToStartPos;
    private boolean isFrozen;
    private GameState state;
    private int id;

    public SeaMonster(World world,Player player, GameState state, int Id){
        Texture texture = new Texture(Gdx.files.internal("Entities/seaMonster.png")); //TODO make better sprite
        gameWorld = world;
        sprite = new Sprite(texture);
        currentState = FiniteState.STAY;
        this.player = player;
        this.state = state;
        this.id = Id;
        health = state.monsterHealths.get(id);

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(state.monsterPositions.get(Id));
        bodyDef.fixedRotation = true;
        body = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;

        body.createFixture(fixtureDef).setUserData("SeaMonster");
        body.setUserData("");
        body.setLinearDamping(1);

        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set(state.monsterStartPositions.get(Id));
        startBody = gameWorld.createBody(bodyDef1);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape;
        fixtureDef1.isSensor = true;
        startBody.createFixture(fixtureDef1);

        //SeaMonster AI behaviour
        entity = new B2dSteeringEntity(body,10);
        targetPlayer = new B2dSteeringEntity(player.getBody(),10);
        targetHome = new B2dSteeringEntity(startBody,10);

        arriveToPlayer = new Arrive<>(entity,targetPlayer)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(10)
                .setDecelerationRadius(10);

        entity.setBehavior(arriveToPlayer);

        arriveToStartPos = new Arrive<>(entity,targetHome)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0)
                .setDecelerationRadius(10);




        this.addActor(new Image(sprite));
        this.setPosition(body.getPosition().x-(sprite.getWidth()/2),body.getPosition().y-(sprite.getHeight()/2));
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        isFrozen = state.isFrozen;

    }

    @Override
    public void act(float delta){
        super.act(delta);


        float distanceFromPlayer = (float) Math.hypot(targetPlayer.getBody().getPosition().y-entity.getBody().getPosition().y,
                targetPlayer.getBody().getPosition().x-entity.getBody().getPosition().x);

        float distanceFromHome = (float) Math.hypot(startBody.getPosition().y-entity.getBody().getPosition().y,
                startBody.getPosition().x-entity.getBody().getPosition().x);

        //Logic on what current state the enemy ship is in
        if (distanceFromPlayer < 150 && distanceFromHome < 300 && currentState != FiniteState.RETURN && !isFrozen){
            currentState = FiniteState.FOLLOW;
            entity.setBehavior(arriveToPlayer);
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

        //Player takes damage if hit
        if (player.isHit() && this.isHit()){
            player.takeDamage(10);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(body.getPosition().x-(sprite.getWidth()/2), body.getPosition().y-(sprite.getHeight()/2));
        this.setRotation((float)Math.toDegrees(body.getAngle()));
        super.draw(batch, parentAlpha);
    }

    private enum FiniteState {
        FOLLOW,
        RETURN,
        STAY
    }
    public boolean isHit(){
        return body.getUserData() == "Hit";
    }
    public void takeDamage(int damage){
        if(health > 0){
            health -=damage;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }
    public void dispose() {
        this.setPosition(-100,-100);
        gameWorld.destroyBody(body);
        gameWorld.destroyBody(startBody);
    }

    public float getHealth(){return health;}

    public void freeze(){isFrozen = true;}

    public void unfreeze(){isFrozen = false;}

    public boolean get_freeze(){return isFrozen;}

    public Integer getId(){
        return id;
    }

    public void updateState(GameState newState){
        newState.monsterHealths.add(health);
        newState.monsterPositions.add(body.getPosition());
        newState.monsterStartPositions.add(startBody.getPosition());
    }
}

