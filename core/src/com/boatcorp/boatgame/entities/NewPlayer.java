package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.boatcorp.boatgame.screens.NewPlayScreen;
import org.jetbrains.annotations.NotNull;

public class NewPlayer extends Actor {

    // TODO think about pixels per meter, isn't implemented at the moment, each pixel of the player sprite takes up a meter in the box2d world.



    private float speed = 100f;

    private Screen screen;


    private Weapon equippedWeapon;
    private NewPlayScreen newPlayScreen;

    private Sprite sprite;
    private World world;
    private Body body;


    public NewPlayer(@NotNull World world, @NotNull NewPlayScreen newPlayScreen) {
        this.world = world;
        this.screen = newPlayScreen;
        this.sprite = new Sprite(new Texture(Gdx.files.internal("16x16px.png")));

        // Set up the box2d hitbox
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setTransform(50, 20, 0);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef);





        equippedWeapon = new Weapon();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        processMovement();
        processAttack();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch);
    }

    private void processMovement() {
        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        if (up && !down) {
            body.setLinearVelocity(0, speed);
        }

        if (down && !up) {
            body.setLinearVelocity(0, -speed);
        }

        if (left && !right) {
            body.setLinearVelocity(-speed, 0);
        }

        if (right && !left) {
            body.setLinearVelocity(speed, 0);
        }

        if (!up && !down && !left && !right) {
            body.setLinearVelocity(0, 0);
        }

        sprite.setCenter(body.getPosition().x, body.getPosition().y);
    }

    private void processAttack() {
        boolean up = Gdx.input.isKeyJustPressed(Input.Keys.UP);
        if (up) {
            equippedWeapon.attack();
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }


}
