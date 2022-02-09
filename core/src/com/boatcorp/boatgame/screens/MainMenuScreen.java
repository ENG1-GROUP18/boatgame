package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;

import static com.boatcorp.boatgame.screens.Constants.PPM;

public class MainMenuScreen implements Screen {

    private Game boatGame;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private Stage stage;

    // For Shader
    private VfxManager vfxManager;
    private BloomEffect effectBloom;
    private OldTvEffect effectTv;
    private RadialDistortionEffect effectDistortion;
    private VignettingEffect effectVignetting;

    public MainMenuScreen(Game boatGame) {
        this.boatGame = boatGame;
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        stage = new Stage(viewport);
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        // Create table
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        // Add labels to table
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/korg.fnt")), Color.WHITE);

        Label label1 = new Label("move with WASD", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.3f);

        Label label2 = new Label("aim and fire with MOUSE", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(0.3f);

        Label label3 = new Label("press SPACE to start", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(0.3f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20);

        // Listener to detect input to progress past menu screen
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    launchGame();
                }
                return true;
            }
        });

        // Configuring shaders
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

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

        stage.draw();

        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen((Gdx.graphics.getWidth() - viewport.getScreenWidth())/2,
                (Gdx.graphics.getHeight() - viewport.getScreenHeight())/2,
                viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    private void launchGame() {
        PointSystem.setPoints(0);
        boatGame.setScreen(new PlayScreen(boatGame, this));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        vfxManager.resize(viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();

        vfxManager.dispose();
        effectBloom.dispose();
        effectTv.dispose();
        effectDistortion.dispose();
        effectVignetting.dispose();
    }
}
