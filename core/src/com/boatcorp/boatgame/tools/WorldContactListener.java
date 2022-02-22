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

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
