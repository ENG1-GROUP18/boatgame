package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;

public class ResultScreen extends BasicMenuScreen {

    private final BoatGame boatGame;
    private static final int WORLD_HEIGHT = Gdx.graphics.getHeight();
    private final String victory;

    public ResultScreen(boolean win, BoatGame game) {
        super(game);
        this.boatGame = game;
        victory = (win) ? "VICTORY" : "GAME OVER";
        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);


        // Add labels to table
        Label label1 = new Label("[NORMAL]Press [HIGHLIGHTED]Enter [NORMAL]to play again", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]Press [HIGHLIGHTED]Esc [NORMAL]to return to main menu", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();


        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.ENTER) {
                    boatGame.changeScreen(BoatGame.screenType.PLAY);
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.ESCAPE) {
                    boatGame.changeScreen(BoatGame.screenType.START_MENU);
                }
                return true;
            }
        });
    }

    @Override
    public void update() {
        super.update();
    }

}
