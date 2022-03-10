package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class newPlayer extends Group {

    private Weapon equippedWeapon;


    public newPlayer() {
        equippedWeapon = new Weapon();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        processMovement();
        processAttack();
    }

    private void processMovement() {
        //boolean up = Gdx.input.isButtonJustPressed(Input.Keys.W);
    }

    private void processAttack() {
        boolean up = Gdx.input.isKeyJustPressed(Input.Keys.UP);
        if (up) {
            equippedWeapon.attack();
        }
    }

}
