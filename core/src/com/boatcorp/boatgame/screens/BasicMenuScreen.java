package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.crashinvaders.vfx.VfxManager;


public class BasicMenuScreen implements Screen {
    /**
     * A base class to use ensure consistency across menus.
     * Create a menu by extending this and adding items to the protected stage.
     */

    // TODO tidy all of this stuff a tiny bit.

    //---------------
    static final boolean ENABLE_TABLE_DEBUG = true;
    //---------------

    protected int jamOnToast = 5;
    private BoatGame boatGame;
    private OrthographicCamera camera;
    private Viewport viewport;
    protected Stage stage;
    private VfxManager vfxManager;
    protected Label.LabelStyle style;


    public BasicMenuScreen(BoatGame game) {
        this.boatGame = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        stage = new Stage(viewport);
        vfxManager = game.getVfxManager();

        style = new Label.LabelStyle(
                new BitmapFont(Gdx.files.internal("fonts/korg.fnt")), Color.WHITE);
       }


    public void update() {
        // Overwrite with code run every frame before render function.
    }

    @Override
    public void render(float delta) {
        update();

        // Clean up buffers
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

        stage.draw();

        // Render with shaders added
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen((Gdx.graphics.getWidth() - viewport.getScreenWidth())/2,
                (Gdx.graphics.getHeight() - viewport.getScreenHeight())/2,
                viewport.getScreenWidth(), viewport.getScreenHeight());
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
        vfxManager.dispose();
        stage.dispose();
    }
}
