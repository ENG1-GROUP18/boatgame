package com.boatcorp.boatgame.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.boatcorp.boatgame.screens.PlayScreen;

public class WorldContactListener implements ContactListener {

    private PlayScreen parent;

    public WorldContactListener (PlayScreen parent){
        this.parent = parent;
    }



    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getUserData() != null && fb.getUserData() != null){
            if (fa.getUserData().toString().equals("College") && fb.getUserData().toString().equals("PlayerBullet")){
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }

            if (fa.getUserData().toString().equals("Player") && fb.getUserData().toString().equals("CollegeBullet")){
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }

            if (fa.getUserData().toString().equals("Player") && fb.getUserData().toString().equals("EnemyShipBullet")){
                System.out.println("here");
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getBody().getUserData() != null && fb.getBody().getUserData() != null) {
            if (fa.getBody().getUserData().toString().equals("Hit")) {
                fa.getBody().setUserData("");
            }
            if (fb.getBody().getUserData().toString().equals("Hit")) {
                fb.getBody().setUserData("");
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
