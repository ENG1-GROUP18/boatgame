package com.boatcorp.boatgame.frameworks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.boatcorp.boatgame.entities.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Creates a Hud Object
 */
public class Hud {
    private final Stage stage;
    private final FitViewport stageViewport;
    private final BitmapFont font;
    private final Label pointScore;
    private final Player player;
    private final ProgressBar.ProgressBarStyle healthBarStyle;
    private final ProgressBar healthBar;
    private final Label plunderScore;
    private final Label updateAlert;
    private final Label shopLabel;

    private Drawable getColouredDrawable(float width, float height, Color color) {
        Pixmap pixmap = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();
        return drawable;
    }
    
    public Hud(SpriteBatch spriteBatch, Player player) {
        this.player = player;
        stageViewport = new FitViewport(640, 480);
        stage = new Stage(stageViewport, spriteBatch);
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);

        pointScore = new Label("Points: " + PointSystem.getPoints(), new Label.LabelStyle(font, Color.WHITE));
        pointScore.setWrap(true);
        pointScore.setFontScale(0.5f);
        pointScore.setPosition(8, 40);
        pointScore.setText("Points: " + PointSystem.getPoints());
        
        plunderScore = new Label("Plunder: " + PlunderSystem.getPlunder(), new Label.LabelStyle(font, Color.WHITE));
        plunderScore.setWrap(true);
        plunderScore.setFontScale(0.5f);
        plunderScore.setPosition(8, 430);
        plunderScore.setText("Plunder: "+ PlunderSystem.getPlunder());

        updateAlert = new Label("", new Label.LabelStyle(font, Color.WHITE));
        updateAlert.setWrap(true);
        updateAlert.setFontScale(0.5f);
        updateAlert.setPosition(350, 430);
        updateAlert.setText("");

        shopLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        shopLabel.setWrap(true);
        shopLabel.setFontScale(0.5f);
        shopLabel.setPosition(300, 40);
        shopLabel.setText("");

        healthBarStyle = new ProgressBar.ProgressBarStyle();

        healthBar = new ProgressBar(0, player.getMaxHealth(), 1f, false, healthBarStyle);
        healthBar.setPosition(8, 20);
        healthBar.setValue(player.getMaxHealth());

        int height = 10;
        healthBarStyle.background = getColouredDrawable(player.getMaxHealth(), height+4, Color.WHITE);
        healthBarStyle.knob = getColouredDrawable(0, height, Color.RED);
        healthBarStyle.knobBefore = getColouredDrawable(player.getHealth(), height, Color.RED);
        healthBarStyle.knobBefore.setRightWidth(2);
        healthBarStyle.background.setLeftWidth(2);
        healthBarStyle.background.setRightWidth(2);

        stage.addActor(pointScore);
        stage.addActor(healthBar);
        stage.addActor(plunderScore);
        stage.addActor(updateAlert);
        stage.addActor(shopLabel);
    }

    public Stage getStage() { return stage; }

    public void setPointScore(String newText) {
        pointScore.setText(newText);
    }

    public void setHealthValue(float newValue) {
        healthBar.setValue(newValue);
        healthBarStyle.knob = getColouredDrawable(2, 10, Color.RED);
        healthBarStyle.knobBefore = getColouredDrawable(newValue, 10, Color.RED);


    }
    
    public void setPlunderScore(String newText) {
        plunderScore.setText(newText);
    }

    public void setUpdateAlert(String newUpdate){updateAlert.setText(newUpdate);}

    public void setShopLabel(String text){shopLabel.setText(text);}

    public void dispose() {
        font.dispose();
        player.dispose();
        stage.dispose();
    }

}


