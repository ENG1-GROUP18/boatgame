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
        table.setDebug(BasicMenuScreen.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/korg.fnt")), Color.WHITE);

        Label label1 = new Label("move with WASD", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.4f);

        Label label2 = new Label("aim and fire with MOUSE", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(0.4f);

        Label label3 = new Label("press SPACE to start", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(0.4f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20);

        // Listener to detect input to progress past menu screen
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
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