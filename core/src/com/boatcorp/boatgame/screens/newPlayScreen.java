package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.entities.PlayerShip;

public class newPlayScreen implements Screen {

    BoatGame game;
    Stage stage;
    Batch batch;


    public newPlayScreen(BoatGame game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();


        
        stage.addActor(new PlayerShip());

    }


    @Override
    public void show() {

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
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
