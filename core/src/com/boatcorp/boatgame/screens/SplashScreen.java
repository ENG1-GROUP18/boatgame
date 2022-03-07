package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.boatcorp.boatgame.BoatGame;

public class SplashScreen extends BasicMenuScreen {

    public SplashScreen(final BoatGame game) {
        super(game);

        // Create items
        Label label1 = new Label("Boatgame", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.6f);

        // Add items to table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        table.add(label1).fillX().uniformX();

        // Add table to stage
        stage.addActor(table);

        Timer.Task myTimerTask = new Timer.Task() {
            @Override
            public void run() {
                game.changeScreen(BoatGame.screenType.START_MENU);
            }
        };
        Timer.schedule(myTimerTask, 2f);
    }


    @Override
    public void update() {

    }
}
