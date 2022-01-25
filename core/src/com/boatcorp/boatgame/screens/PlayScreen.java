package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.tools.MapLoader;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.entities.Player;

import static com.boatcorp.boatgame.screens.Constants.*;

public class PlayScreen implements Screen {

    private final SpriteBatch batch;
    private final SpriteBatch fontBatch;
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final MapLoader mapLoader;
    private final BitmapFont font;
    private final Texture playerTexture;
    private final Player player;

    public PlayScreen() {
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        world = new World(GRAVITY, true);
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        camera.zoom = DEFAULT_ZOOM;
        viewport = new FitViewport(640 / PPM, 480 / PPM, camera);
        mapLoader = new MapLoader();
        playerTexture = new Texture(Gdx.files.internal("Maps/boat1.png"));
        Sprite playerSprite = new Sprite(playerTexture);
        player = new Player(playerSprite, 0, 0);
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        draw();
    }

    private void draw() {
        // Batch drawing
        batch.setProjectionMatrix(camera.combined);


        b2dr.render(world, camera.combined);
        mapLoader.render(camera);

        batch.begin();
        batch.draw(playerTexture, player.x, player.y);
        batch.end();

        // FontBatch drawing
        fontBatch.begin();
        font.getData().setScale(0.5f);
        String displayPoint = "SCORE:" + PointSystem.getPoints();
        font.draw(fontBatch, displayPoint, 8, 472);
        fontBatch.end();
    }

    private void update(final float delta) {
        camera.position.set(player.getPosition(), 0);
        camera.update();
        world.step(delta, 6,2);

        // Player updates
        player.update(delta);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,(float)width/16,(float)height/16);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        fontBatch.dispose();
        world.dispose();
        b2dr.dispose();
        mapLoader.dispose();
    }
}
