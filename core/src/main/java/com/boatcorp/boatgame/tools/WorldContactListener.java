package com.boatcorp.boatgame.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.boatcorp.boatgame.screens.PlayScreen;

/**
 * This class listens to any contacts between bodies during the game and performs the necessary actions when they collide.
 * This has been added in assessment 2 to improve the collision system for enemy ships, sea monsters and others. - USR10.
 */
public class WorldContactListener implements ContactListener {

    public WorldContactListener (PlayScreen parent){
    }


    /**
     * For each collision which occurs in the world, it checks which objects have collided and performs the appropriate actions
     * @param contact a contact object which holds the info on all the contacts on this frame
     */
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
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }

            if (fa.getUserData().toString().equals("EnemyShip") && fb.getUserData().toString().equals("PlayerBullet")){
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }
            if (fa.getUserData().toString().equals("Player") && fb.getUserData().toString().equals("SeaMonster")){
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }
            if (fa.getUserData().toString().equals("SeaMonster") && fb.getUserData().toString().equals("PlayerBullet")){
                fa.getBody().setUserData("Hit");
                fb.getBody().setUserData("Hit");
            }
        }

    }

    /**
     * Performs the actions needed to tell the system that the bodies are no longer colliding
     * @param contact a contact object which holds the info on all the contacts on this frame
     */
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
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
