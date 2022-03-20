package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    private BoatGame boatGame;
    private OrthographicCamera camera;
    private Viewport viewport;
    protected Stage stage;
    private VfxManager vfxManager;
    protected Label.LabelStyle style;
    protected Pixmap pixmap;
    protected Texture texture;
    protected SpriteBatch batch;


    public BasicMenuScreen(BoatGame game) {
        this.boatGame = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(640, 480, camera);
        stage = new Stage(viewport);
        vfxManager = game.getVfxManager();

        // Style to be applied to labels
        style = new Label.LabelStyle(
                new BitmapFont(Gdx.files.internal("fonts/PressStart2P.fnt")), Color.WHITE);
        style.font.getData().markupEnabled = true;

        // Custom colours
        Colors.put("NORMAL", new Color(225/255f, 225/255f, 225/255f, 1));
        Colors.put("HIGHLIGHTED", new Color(200/255f, 35/255f, 75/255f, 1));

        // Solid color texture created to use as screen background
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(10/255f, 10/255f, 10/255f, 1));
        pixmap.fill();
        texture = new Texture(pixmap);
        batch = new SpriteBatch();
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

        // Draw background texture
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(texture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();

        stage.draw();

        // Render with shaders added
        vfxManager.endInputCapture();
        if (boatGame.ENABLE_SHADERS) {
            vfxManager.applyEffects();
        }

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
        pixmap.dispose();
        texture.dispose();
    }
}
