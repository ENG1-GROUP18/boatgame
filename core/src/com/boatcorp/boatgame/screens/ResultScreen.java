package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.PointSystem;

public class ResultScreen implements Screen {

    private final BoatGame boatGame;
    private static final int WORLD_HEIGHT = Gdx.graphics.getHeight();
    private final SpriteBatch fontBatch;
    private final BitmapFont font;
    private final String victory;
    private final Viewport viewport;

    public ResultScreen(boolean win, BoatGame game) {
        this.boatGame = game;
        victory = (win) ? "VICTORY" : "GAME OVER";
        viewport = new ExtendViewport(0, WORLD_HEIGHT);
        fontBatch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        fontBatch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        fontBatch.begin();
        font.getData().setScale(0.5f);
        GlyphLayout victoryGlyph = new GlyphLayout(font, this.victory);
        GlyphLayout enterGlyph = new GlyphLayout(font, "Press Enter");

        font.draw(fontBatch, this.victory, viewport.getScreenWidth() / 2f - victoryGlyph.width / 2, viewport.getScreenHeight() / (4f/3f));
        font.draw(fontBatch, "Press Enter", viewport.getScreenWidth() / 2f - enterGlyph.width / 2, viewport.getScreenHeight() / 4f);
        fontBatch.end();
        checkInputs();
    }

    private void checkInputs() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER) ) {
            PointSystem.setPoints(0);
            boatGame.setScreen(new PlayScreen(boatGame));
        }

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
    public void hide() {

    }

    @Override
    public void dispose() {
        fontBatch.dispose();
        font.dispose();
    }
}
