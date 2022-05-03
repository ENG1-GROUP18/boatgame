package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.boatcorp.boatgame.BoatGame;

/**
 * This screen allows the user to choose what difficulty the game is.
 * This was added in assessment 2 to fulfil the  user being able to adjust the difficulty - USR21
 */
public class ModeScreen extends BasicMenuScreen{


    public ModeScreen(final BoatGame game) {
        super(game);

        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label label1 = new Label("[NORMAL]play on easy with [HIGHLIGHTED]E", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]play on normal with [HIGHLIGHTED]N", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);

        Label label3 = new Label("[NORMAL]play on hard with [HIGHLIGHTED]H", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(1f);


        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20);

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.E) {
                    game.difficulty = 0;
                    game.changeScreen(BoatGame.screenType.PLAY);
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.N) {
                    game.difficulty = 1;
                    game.changeScreen(BoatGame.screenType.PLAY);
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.H) {
                    game.difficulty = 2;
                    game.changeScreen(BoatGame.screenType.PLAY);
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
