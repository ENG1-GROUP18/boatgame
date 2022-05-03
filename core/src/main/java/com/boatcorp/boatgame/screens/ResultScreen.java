package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.boatcorp.boatgame.BoatGame;

/**
 * This class gives the screen which displays weather the user has won or lost at the end of the game.
 * In assessment 2 the way the text was implemented is replaced by a table to keep constant with the rest of the screens.
 */
public class ResultScreen extends BasicMenuScreen {

    private final BoatGame boatGame;
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
