package com.boatcorp.boatgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.boatcorp.boatgame.screens.*;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.google.gson.Gson;

/**
 *  Main class which initialises the Game.
 */
public class BoatGame extends Game {

	//---------------
	public final boolean ENABLE_SHADERS = true;
	public final boolean ENABLE_BOX2D_WIREFRAME = true;
	public final boolean ENABLE_TABLE_DEBUG = false;
	public boolean HEADLESS = false;
	public int difficulty;
	//---------------


	// For a universal set of shaders
	private VfxManager vfxManager;
	private OldTvEffect effectTv;
	private VignettingEffect effectVignetting;
	private RadialDistortionEffect effectDistortion;
	private BloomEffect effectBloom;
	private NfaaEffect effectAA;

	// Screens
	private SplashScreen splashScreen;
	private StartMenuScreen startMenuScreen;
	private PlayScreen playScreen;
	private PauseScreen pauseScreen;
	private ShopScreen shopScreen;
	private EndScreen endScreen;
	private SaveScreen saveScreen;
	private ModeScreen modeScreen;


	public enum screenType {
		SPLASH,
		START_MENU,
		PLAY,
		PAUSE_MENU,
		SHOP,
		END_MENU,
		SAVE,
		MODE
	}

	@Override
	public void create () {
		if (!HEADLESS){ //Shaders don't work in headless mode for testing due to implementation
			setUpShaders();
		}

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
					GameState g = new GameState();
					g.difficulty = difficulty;
					playScreen = new PlayScreen(this, new GameState());
				}
				setScreen(playScreen);
				break;

			case PAUSE_MENU:
				if (pauseScreen == null) {
					pauseScreen = new PauseScreen(this, new GameState());
				}
				setScreen(pauseScreen);
				break;

			case SHOP:
				if (shopScreen == null) {
					shopScreen = new ShopScreen(this, new GameState());
				}
				setScreen(shopScreen);
				break;

			case END_MENU:
				if (endScreen == null) {
					endScreen = new EndScreen(this);
				}
				setScreen(endScreen);
				break;

			case SAVE:
				if (saveScreen == null) {
					saveScreen = new SaveScreen(this);
				}
				setScreen(saveScreen);
				break;
			case MODE:
				if (modeScreen == null) {
					modeScreen = new ModeScreen(this);
				}
				setScreen(modeScreen);
				break;
		}
	}

	private void setUpShaders() {
		// Configuring shaders
		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

		effectTv = new OldTvEffect();
		effectTv.setTime(0f);

		effectVignetting = new VignettingEffect(false);
		effectVignetting.setIntensity(0.8f);
		effectVignetting.setSaturation(0.2f);

		effectDistortion = new RadialDistortionEffect();
		effectDistortion.setDistortion(0.1f);

		effectBloom = new BloomEffect();
		effectBloom.setThreshold(0.2f);
		effectBloom.setBloomIntensity(1.2f);
		effectBloom.setBaseIntensity(1.2f);

		effectAA = new NfaaEffect(false);

		// Add shaders to manager, order matters
		vfxManager.addEffect(effectTv);
		vfxManager.addEffect(effectVignetting);
		vfxManager.addEffect(effectDistortion);
		vfxManager.addEffect(effectBloom);
		//vfxManager.addEffect(effectAA);

	}

	@Override
	public void dispose() {
		vfxManager.dispose();
		effectBloom.dispose();
		effectTv.dispose();
		effectDistortion.dispose();
		effectVignetting.dispose();
		effectAA.dispose();
	}

	public VfxManager getVfxManager() {
		return vfxManager;
	}
	
	public void saveGame(String numSave){
		Gson gson = new Gson();
		String json = gson.toJson(playScreen.getState(),GameState.class);
		Preferences p = Gdx.app.getPreferences("SAVEDGAME");
		p.putString(numSave, json);
		p.flush();
	}

	public void loadGame(String numSave){
		Gson gson = new Gson();
		Preferences p = Gdx.app.getPreferences("SAVEDGAME");
		String gameString = p.getString(numSave,"absent");
		if (gameString == "absent"){
			this.changeScreen(BoatGame.screenType.PLAY);}
		else {
			GameState loader = gson.fromJson(gameString, GameState.class);
			playScreen = new PlayScreen(this, loader);
			this.setScreen(playScreen);}
	}

	public void setHeadless(){
		HEADLESS = true;
	}
}
