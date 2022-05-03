package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.boatcorp.boatgame.BoatGame;

/**
 * This class contains the screen to get which save game state the user wants to pick.
 * This was added in assessment 2 to fulfil the requirement of a saving a game - USR20.
 */
public class SaveScreen extends BasicMenuScreen{


    public SaveScreen(final BoatGame game) {
        super(game);

        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label label1 = new Label("[NORMAL]load memory slot 1 with [HIGHLIGHTED]1", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]load memory slot 2 with [HIGHLIGHTED]2", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);

        Label label3 = new Label("[NORMAL]load memory slot 3 with [HIGHLIGHTED]3", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(1f);

        Label label4 = new Label("[NORMAL]load memory slot 4 with [HIGHLIGHTED]4", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);

        Label label5 = new Label("[NORMAL]press [HIGHLIGHTED]SPACE [NORMAL]to return to menu", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20).row();
        table.add(label4).fillX().uniformX().pad(20).row();
        table.add(label5).fillX().uniformX().pad(20);

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.NUM_1) {
                    game.loadGame("1");
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.NUM_2) {
                    game.loadGame("2");
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.NUM_3) {
                    game.loadGame("3");
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.NUM_4) {
                    game.loadGame("4");
                }
                return true;
            }
        });

        // Listener to detect input to progress past menu screen
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    game.changeScreen(BoatGame.screenType.START_MENU);
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
