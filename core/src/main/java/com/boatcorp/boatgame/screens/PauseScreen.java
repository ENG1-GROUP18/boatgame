package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;

public class PauseScreen extends BasicMenuScreen{

    private GameState state;

    public PauseScreen(final BoatGame game, final GameState state) {
        super(game);
        this.state = state;
        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label label1 = new Label("[NORMAL]RESUME WITH [HIGHLIGHTED]SPACE", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]QUIT GAME WITH [HIGHLIGHTED]E", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);



        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20);

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    PlayScreen playScreen = new PlayScreen(game,state);
                    game.setScreen(playScreen);
                }
                return true;
            }
        });
        
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.E) {
                    Gdx.app.exit();
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
