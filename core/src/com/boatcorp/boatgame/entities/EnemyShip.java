package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.boatcorp.boatgame.GameState;

public class EnemyShip extends Group {
    private final Sprite sprite;
    private float health = 100;
    private final Body bodyd;
    private final World gameWorld;
    private GameState gameState;

    public EnemyShip(World world, GameState state, String ID, Vector2 position){
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


        this.addActor(new Image(sprite));
        this.setPosition(position.x,position.y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(bodyd.getPosition().x-(sprite.getWidth()/2), bodyd.getPosition().y-(sprite.getHeight()/2));
        super.draw(batch, parentAlpha);
    }
}
