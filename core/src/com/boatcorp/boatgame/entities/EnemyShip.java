package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
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

public class EnemyShip extends Group {
    private final Sprite sprite;
    private final Vector2 position;
    private float health = 100;
    private final Body bodyd;
    private final World gameWorld;
    private B2dSteeringEntity entity,targetP,targetC;
    private Arrive<Vector2> arriveToPlayer,arriveToStartPos;
    private Player player;
    private GameState gameState;
    private FiniteState currentState;

    //TODO simplify/cleanup so not so many objects are passed in
    public EnemyShip(World world, GameState state, String ID, Vector2 position,Player player,College college){
        Texture texture = new Texture(Gdx.files.internal("Entities/boat2.png"));
        sprite = new Sprite(texture);
        gameWorld = world;
        gameState = state;
        this.position = position;

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        bodyd = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        bodyd.createFixture(fixtureDef).setUserData("EnemyShip"+ID);
        bodyd.setUserData("");
        bodyd.setLinearDamping(5);

        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set(position);
        Body startBody = gameWorld.createBody(bodyDef1);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape;
        fixtureDef1.isSensor = true;
        startBody.createFixture(fixtureDef1);


        //Ship AI behaviour
        entity = new B2dSteeringEntity(bodyd,10);
        targetP = new B2dSteeringEntity(player.getBody(),10);
        targetC = new B2dSteeringEntity(startBody,10);

        currentState = FiniteState.FOLLOW;
        arriveToPlayer = new Arrive<>(entity,targetP)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(50)
                .setDecelerationRadius(10);

        entity.setBehavior(arriveToPlayer);

        arriveToStartPos = new Arrive<>(entity,targetC)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0)
                .setDecelerationRadius(10);


        this.addActor(new Image(sprite));
        this.setPosition(position.x,position.y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float dist = (float) Math.hypot(targetP.getBody().getPosition().y-entity.getBody().getPosition().y,
                targetP.getBody().getPosition().x-entity.getBody().getPosition().x);

        if (dist < 150){
            entity.setBehavior(arriveToPlayer); //TODO change so only sets on state change, could cause lag otherwise
            currentState = FiniteState.FOLLOW;
            entity.update(delta);
        } else if (!entity.getBody().getPosition().equals(targetC.getBody().getPosition())){
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
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));
        this.setRotation((float)Math.toDegrees(bodyd.getAngle()));
        super.draw(batch, parentAlpha);
    }

    public enum FiniteState {
        FOLLOW,
        RETURN,
        STAY
    }

    public String getState() {
        return currentState.toString();
    }
}
