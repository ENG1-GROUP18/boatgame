package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.PointSystem;

public class MainMenuScreen implements Screen {



    private Game boatGame;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private Stage stage;

    public MainMenuScreen(Game boatGame) {
        this.boatGame = boatGame;
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        // Set up stage
        stage = new Stage(viewport);


        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        // Add labels to table
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/korg.fnt")), Color.WHITE);

        Label label1 = new Label("press SPACE to start", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.5f);

//        Label label2 = new Label("LABLE 2", style);
//        label2.setAlignment(Align.center);
//        label2.setFontScale(0.5f);


        table.add(label1).fillX().uniformX();
//        table.add(label2).fillX().uniformX().pad(80);


        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    launchGame();
                }
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    private void launchGame() {
        PointSystem.setPoints(0);
        boatGame.setScreen(new PlayScreen(boatGame, this));

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    }
}
