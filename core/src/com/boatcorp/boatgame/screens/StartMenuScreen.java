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

public class StartMenuScreen extends BasicMenuScreen{


    public StartMenuScreen(final BoatGame game) {
        super(game);

        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label label1 = new Label("[NORMAL]move with [HIGHLIGHTED]WASD", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]aim and fire with [HIGHLIGHTED]ARROW KEYS", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);

        Label label3 = new Label("[NORMAL]save to the 4 memory slots \n with [HIGHLIGHTED]1, 2, 3 or 4", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(1f);

        Label label4 = new Label("[NORMAL]press [HIGHLIGHTED]SPACE [NORMAL]to start", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);

        Label label5 = new Label("[NORMAL]press [HIGHLIGHTED]ENTER [NORMAL]to browse saved games", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20).row();
        table.add(label4).fillX().uniformX().pad(20).row();
        table.add(label5).fillX().uniformX().pad(20);

        // Listener to detect input to progress past menu screen
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    game.changeScreen(BoatGame.screenType.PLAY);
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.ENTER) {
                    game.changeScreen(BoatGame.screenType.SAVE);
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

