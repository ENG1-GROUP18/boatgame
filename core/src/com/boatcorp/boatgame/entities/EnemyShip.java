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

public class EnemyShip extends Group {
    private final Sprite sprite;
    private float health = 100;
    private final Body bodyd;
    private final World gameWorld;
    private B2dSteeringEntity entity,target;
    private Player player;
    private GameState gameState;

    public EnemyShip(World world, GameState state, String ID, Vector2 position,Player player){
        Texture texture = new Texture(Gdx.files.internal("Entities/boat2.png"));
        sprite = new Sprite(texture);
        gameWorld = world;
        gameState = state;

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
        //Ship AI behaviour
        entity = new B2dSteeringEntity(bodyd,10);
        target = new B2dSteeringEntity(player.getBody(),10);
        Arrive<Vector2> arrive = new Arrive<>(entity,target)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(50)
                .setDecelerationRadius(10);

        entity.setBehavior(arrive);


        this.addActor(new Image(sprite));
        this.setPosition(position.x,position.y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float dist = (float) Math.atan2(target.getBody().getPosition().y-entity.getBody().getPosition().y,
                target.getBody().getPosition().x-entity.getBody().getPosition().x);

        System.out.println(dist);
        entity.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));
        this.setRotation((float)Math.toDegrees(bodyd.getAngle()));
        super.draw(batch, parentAlpha);
    }
}
