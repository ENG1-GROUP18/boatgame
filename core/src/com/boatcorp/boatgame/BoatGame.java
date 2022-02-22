package com.boatcorp.boatgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Pixmap;
import com.boatcorp.boatgame.screens.*;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;


public class BoatGame extends Game {

	// For a universal set of shaders
	private VfxManager vfxManager;
	private OldTvEffect effectTv;
	private VignettingEffect effectVignetting;
	private RadialDistortionEffect effectDistortion;
	private BloomEffect effectBloom;

	// Screens
	private SplashScreen splashScreen;
	private StartMenuScreen startMenuScreen;
	private PlayScreen playScreen;
	private PauseScreen pauseScreen;
	private ShopScreen shopScreen;
	private EndScreen endScreen;


	public enum screenType {
		SPLASH,
		START_MENU,
		PLAY,
		PAUSE,
		SHOP,
		END_MENU
	}

	@Override
	public void create () {
		setUpShaders();
		splashScreen = new SplashScreen(this);
		changeScreen(screenType.SPLASH);
	}

	public void changeScreen(screenType type) {
		// Change screen, create new object if null
		switch (type) {
			case SPLASH:
				if (splashScreen == null) {
					splashScreen = new SplashScreen(this);
				}
				setScreen(splashScreen);
				break;

			case START_MENU:
				if (startMenuScreen == null) {
					startMenuScreen = new StartMenuScreen(this);
				}
				setScreen(startMenuScreen);
				break;

			case PLAY:
				if (playScreen == null) {
					playScreen = new PlayScreen(this);
				}
				setScreen(playScreen);
				break;
			case PAUSE:
				if (pauseScreen == null) {
					pauseScreen = new PauseScreen(this);
				}
				setScreen(pauseScreen);
				break;

			case SHOP:
				if (shopScreen == null) {
					shopScreen = new ShopScreen(this);
				}
				setScreen(shopScreen);
				break;

			case END_MENU:
				if (endScreen == null) {
					endScreen = new EndScreen(this);
				}
				setScreen(endScreen);
				break;
		}



	}

	private void setUpShaders() {
		// Configuring shaders
		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

		effectTv = new OldTvEffect();
		effectTv.setTime(0.2f);

		effectVignetting = new VignettingEffect(false);
		effectVignetting.setIntensity(0.8f);
		effectVignetting.setSaturation(0.2f);

		effectDistortion = new RadialDistortionEffect();
		effectDistortion.setDistortion(0.1f);

		effectBloom = new BloomEffect();

		// Add shaders to manager, order matters
		vfxManager.addEffect(effectTv);
		vfxManager.addEffect(effectVignetting);
		vfxManager.addEffect(effectDistortion);
		vfxManager.addEffect(effectBloom);
	}

	@Override
	public void dispose() {
		vfxManager.dispose();
		effectBloom.dispose();
		effectTv.dispose();
		effectDistortion.dispose();
		effectVignetting.dispose();
	}

	public VfxManager getVfxManager() {
		return vfxManager;
	}

}
