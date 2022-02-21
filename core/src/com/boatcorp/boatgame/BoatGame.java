package com.boatcorp.boatgame;

import com.badlogic.gdx.Game;
import com.boatcorp.boatgame.screens.MainMenuScreen;

/**
 *  Main class which initialises the Game.
 */
public class BoatGame extends Game {
	
	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
