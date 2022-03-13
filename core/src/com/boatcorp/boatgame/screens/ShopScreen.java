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

public class ShopScreen extends BasicMenuScreen{


    public ShopScreen(final BoatGame game) {
        super(game);

        // Create table
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(game.ENABLE_TABLE_DEBUG);
        stage.addActor(table);

        // Add labels to table
        Label label1 = new Label("[HIGHLIGHTED]MACRO SHOP!", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(1f);

        Label label2 = new Label("[NORMAL]buy the R macro with[HIGHLIGHTED] R!\n[NORMAL]toggle [HIGHLIGHTED]R [NORMAL]for fire bullets \n increasing damage to enemy ships", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(1f);

        Label label3 = new Label("[NORMAL]buy the G macro with[HIGHLIGHTED] G! \n[NORMAL]toggle [HIGHLIGHTED]G [NORMAL]for slime bullets\n poisoning sea monsters", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(1f);

        Label label4 = new Label("[NORMAL]buy the H macro with[HIGHLIGHTED] H! \n[NORMAL]press [HIGHLIGHTED]H [NORMAL]to trade health for plunder\n at any time", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);


        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20).row();
        table.add(label4).fillX().uniformX().pad(20);
        

    }

    @Override
    public void update() {
        super.update();
    }
}
