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
import com.boatcorp.boatgame.tools.B2dSteeringEntity;

public class SeaMonster extends Group {
    Sprite sprite;
    private final Body body;
    private final Body startBody;
    private B2dSteeringEntity entity,targetP,targetC;
    private Arrive<Vector2> arriveToPlayer,arriveToStartPos;

    public SeaMonster(Vector2 position, World gameWorld,Player player){
        Texture texture = new Texture(Gdx.files.internal("Entities/seaMonster.png")); //TODO make better sprite
        sprite = new Sprite(texture);

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;

        body.createFixture(fixtureDef).setUserData("EnemyShip");
        body.setUserData("");
        body.setLinearDamping(1);

        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set(position);
        startBody = gameWorld.createBody(bodyDef1);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape;
        fixtureDef1.isSensor = true;
        startBody.createFixture(fixtureDef1);

        //SeaMonster AI behaviour
        entity = new B2dSteeringEntity(body,10);
        targetP = new B2dSteeringEntity(player.getBody(),10);
        targetC = new B2dSteeringEntity(startBody,10);

        arriveToPlayer = new Arrive<>(entity,targetP)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(10)
                .setDecelerationRadius(10);

        entity.setBehavior(arriveToPlayer);

        arriveToStartPos = new Arrive<>(entity,targetC)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0)
                .setDecelerationRadius(10);




        this.addActor(new Image(sprite));
        this.setPosition(position.x-(sprite.getWidth()/2),position.y-(sprite.getHeight()/2));
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);

    }

    @Override
    public void act(float delta){
        super.act(delta);

        float distanceFromPlayer = (float) Math.hypot(targetP.getBody().getPosition().y-entity.getBody().getPosition().y,
                targetP.getBody().getPosition().x-entity.getBody().getPosition().x);

        float distanceFromHome = (float) Math.hypot(startBody.getPosition().y-entity.getBody().getPosition().y,
                startBody.getPosition().x-entity.getBody().getPosition().x);

        //Logic on what current state the enemy ship is in
        if (distanceFromPlayer < 150 && distanceFromHome < 300){
            entity.setBehavior(arriveToPlayer);
            entity.update(delta);
        } else if (!entity.getBody().getPosition().equals(targetC.getBody().getPosition())){
            entity.setBehavior(arriveToStartPos);
            entity.update(delta);

        } else{
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
}
