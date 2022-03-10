package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.entities.newPlayer;

public class newPlayScreen implements Screen {

    //TODO notes:
    // batch might not be needed, stage has its own.


    BoatGame game;
    Stage stage;
    Batch batch;

    Camera camera;
    Viewport viewport;


    public newPlayScreen(BoatGame game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();

        stage.addActor(new newPlayer());
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }




    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
    }
}
