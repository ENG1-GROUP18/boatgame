package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.crashinvaders.vfx.VfxManager;
import com.boatcorp.boatgame.GameState;
import com.crashinvaders.vfx.effects.*;
public class PauseScreen extends BasicMenuScreen {

    private BoatGame boatGame;

    // For Shader
    private VfxManager vfxManager;
    private OldTvEffect effectTv;
    private RadialDistortionEffect effectDistortion;
    private VignettingEffect effectVignetting;

    public PauseScreen(BoatGame game) {
        super(game);
        // Create table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add labels to table
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("fonts/korg.fnt")), Color.WHITE);

        Label label1 = new Label("PAUSE MENU", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.3f);

        Label label2 = new Label("press E to quit the game", style);
        label2.setAlignment(Align.center);
        label2.setFontScale(0.3f);

        Label label3 = new Label("press SPACE to resume the game", style);
        label3.setAlignment(Align.center);
        label3.setFontScale(0.3f);

        table.add(label1).fillX().uniformX().pad(20).row();
        table.add(label2).fillX().uniformX().pad(20).row();
        table.add(label3).fillX().uniformX().pad(20);

        // Listener to detect input to resume the game
        stage.addListener(new InputListener(){
            public boolean keyDown(InputEvent event, int keycode){
                if (keycode == Input.Keys.SPACE) {
                    resume();
                }
                return true;
            }
        });
        // Create shaders
        effectTv = new OldTvEffect();
        effectVignetting = new VignettingEffect(false);
        effectDistortion = new RadialDistortionEffect();

        // Configuring shaders
        effectTv.setTime(0.2f);
        effectVignetting.setIntensity(0.8f);
        effectVignetting.setSaturation(0.2f);
        effectDistortion.setDistortion(0.1f);

        // resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    public void resume(){
        //TODO: go back to previous scene
    }
}
