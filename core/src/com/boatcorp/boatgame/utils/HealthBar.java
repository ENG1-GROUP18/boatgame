package com.boatcorp.boatgame.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

/**
 * Creates a HealthBar object
 */
public class HealthBar {
    private final ShapeRenderer health;
    private final ShapeRenderer border;

    /**
     * Initialises the Health bar
     */
    public HealthBar() {
        border = new ShapeRenderer();
        health = new ShapeRenderer();
    }

    /**
     * Draws the health bar above the object and displays its health
     * @param position the position to draw the health bar
     * @param maxHealth the max length of the bar
     * @param currentHealth the current length of health inside the bar
     * @param scale how large the bar should be
     */
    public void draw(@NotNull Vector2 position, float maxHealth, float currentHealth, float scale) {
        //TODO change way health bar is drawn
        // Rather than using the max health for the length, set a bar length and then barlength*(currenthp/maxhp) it


        float x = position.x;
        float y = position.y;

        // Draws red health bar to a length in ratio with the current health
        health.begin(ShapeRenderer.ShapeType.Line);

        int lineWidth = 20;
        Gdx.gl20.glLineWidth(lineWidth);

        health.setColor(Color.RED);
        health.line(x, y, x + scale*(maxHealth * currentHealth/maxHealth), y);
        health.end();

        // Draws border around maximum length of the health bar
        border.begin(ShapeRenderer.ShapeType.Line);

        int borderWidth = 4;
        Gdx.gl20.glLineWidth(borderWidth);

        border.setColor(Color.WHITE);
        border.line(x , y + borderWidth, x + scale*maxHealth, y + borderWidth);
        border.line(x, y - borderWidth, x + scale*maxHealth, y - borderWidth);
        border.line(x , y + borderWidth, x, y - borderWidth);
        border.line(x + scale*maxHealth, y + borderWidth, x + scale*maxHealth, y - borderWidth);
        border.end();
    }

    /**
     * Sets the correct batch projecting matrix
     * @param camera used to set the projection matrix to the correct amount inside the batch renderer
     */
    public void setMatrix(Matrix4 camera) {
        health.setProjectionMatrix(camera);
        border.setProjectionMatrix(camera);
    }

    /**
     * Disposes of each of the unneeded objects
     */
    public void dispose() {
        border.dispose();
        health.dispose();
    }
}
