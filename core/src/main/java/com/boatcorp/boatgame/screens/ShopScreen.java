package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.GameState;

/**
 * This class provides the screen of the text to inform the user of what can be purchased with their plunder.
 * This was added in assessment 2 to help fulfil the requirement of spending plunder - USR25.
 */
public class ShopScreen extends BasicMenuScreen{

    private GameState state;

    public ShopScreen(final BoatGame game, final GameState state) {
        super(game);
        this.state = state;
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

        Label label4 = new Label("[NORMAL]buy the H macro with[HIGHLIGHTED] H! \n[NORMAL]press [HIGHLIGHTED]H [NORMAL]to trade plunder for health\n at any time", style);
        label4.setAlignment(Align.center);
        label4.setFontScale(1f);

        Label label5 = new Label("[HIGHLIGHTED]50 plunder [NORMAL]a piece", style);
        label5.setAlignment(Align.center);
        label5.setFontScale(1f);

        Label label6 = new Label("[HIGHLIGHTED]NOTE: [NORMAL] You must be in the store \nto purchase an item", style);
        label6.setAlignment(Align.center);
        label6.setFontScale(0.8f);

        Label label7 = new Label("[NORMAL]return with [HIGHLIGHTED]SPACE", style);
        label7.setAlignment(Align.center);
        label7.setFontScale(1f);


        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20).row();
        table.add(label4).fillX().uniformX().pad(20).row();
        table.add(label5).fillX().uniformX().pad(20).row();
        table.add(label6).fillX().uniformX().pad(20).row();
        table.add(label7).fillX().uniformX().pad(20);

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.R) {
                    if (state.plunder > 50 && !state.hasBoughtRed){
                        state.plunder -= 50;
                        state.hasBoughtRed = true;
                    }
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.G) {
                    if (state.plunder > 50 && !state.hasBoughtGreen){
                        state.plunder -= 50;
                        state.hasBoughtGreen = true;
                    }
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.H) {
                    if (state.plunder > 50 && !state.hasBoughtRed){
                        state.plunder -= 50;
                        state.hasBoughtHealth = true;
                    }
                }
                return true;
            }
        });

        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    PlayScreen playScreen = new PlayScreen(game,state);
                    game.setScreen(playScreen);
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
