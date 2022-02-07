package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.entities.College;
import com.boatcorp.boatgame.entities.Player;
import com.boatcorp.boatgame.frameworks.Hud;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.tools.MapLoader;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;

import java.util.ArrayList;

import static com.boatcorp.boatgame.screens.Constants.*;

public class PlayScreen implements Screen {

    private final Game boatGame;
    private final SpriteBatch batch;
    private final SpriteBatch fontBatch;
    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final MapLoader mapLoader;
    private final BitmapFont font;
    private final Player player;
    private final ArrayList<College> colleges;
    private final Hud hud;


    // test stuff
    private ShapeRenderer shapeRenderer;
    private VfxManager vfxManager;
    private BloomEffect effectBloom;
    private OldTvEffect effectTv;
    private RadialDistortionEffect effectDistortion;
    private VignettingEffect effectVignetting;
    private FxaaEffect effectFxaa;


    private com.crashinvaders.vfx.effects.BloomEffect bloomEffect;


    public PlayScreen(Game game, Screen oldScreen) {
        oldScreen.dispose();
        this.boatGame = game;
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        world = new World(GRAVITY, true);
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640 / PPM, 480 / PPM, camera);

        mapLoader = new MapLoader();
        player = new Player(camera);
        colleges = new ArrayList<>();
        colleges.add(new College("langwith"));
        colleges.add(new College("james"));
        colleges.add(new College("goodricke"));
        collegeSpread();
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
        hud = new Hud(fontBatch, player);

        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        effectBloom = new BloomEffect();
        effectBloom.setBlurAmount(2f);
        effectBloom.setThreshold(5f);
        effectTv = new OldTvEffect();
        effectTv.setTime(0.2f);
        effectDistortion = new RadialDistortionEffect();
        effectDistortion.setDistortion(0.1f);
        effectVignetting = new VignettingEffect(false);
        effectVignetting.setIntensity(0.8f);
        effectVignetting.setSaturation(0.2f);
        effectFxaa = new FxaaEffect();

        effectBloom = new BloomEffect();





        vfxManager.addEffect(effectTv);
        vfxManager.addEffect(effectBloom);
        vfxManager.addEffect(effectDistortion);
        vfxManager.addEffect(effectVignetting);
        vfxManager.addEffect(effectFxaa);

    }

    private void collegeSpread() {
        // TODO: Implement function
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();


        update(delta);
        // Batch drawing
        player.setMatrix(camera.combined);
        for (College college : colleges) {
            college.setMatrix(camera.combined);
        }
        batch.setProjectionMatrix(camera.combined);
        b2dr.render(world, camera.combined);
        mapLoader.render(camera);

        for (College college : colleges) {
            college.draw();
        }
        player.draw();


        fontBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.setPointScore("Points: " + PointSystem.getPoints());
        hud.setHealthValue(player.getHealth());
        hud.getStage().draw();

        hud.getStage().act(delta);

        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();

        combat();
    }

    //TODO rename this, maybe implement directly into act/render method
    private void combat() {
        if (player.isDead()) {
            player.dispose();
            for(College college : colleges) {
                college.dispose();
            }
            boatGame.setScreen(new resultScreen(false, boatGame, this));
        }
        if (colleges.isEmpty()) {
            boatGame.setScreen(new resultScreen(true, boatGame, this));
        }
        for (int i = 0; i < colleges.size(); i++) {
            College college = colleges.get(i);
            if (college.isAlive()) {
                college.combat(player.getPosition(), camera.combined, player);
            } else {
                colleges.remove(college);
                PointSystem.incrementPoint(500);
            }
        }
        player.combat(camera.combined, colleges);
    }

    private void update(final float delta) {
        camera.zoom = DEFAULT_ZOOM;

        // Get properties of the map from the TileMap
        MapProperties prop = mapLoader.getMap().getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);

        // Using `lerping` to slightly lag camera behind player //TODO modify this, player gets too close to edge of screen
        float lerp = 5f;
        Vector2 playerPos = player.getPosition();
        camera.position.x += (playerPos.x - camera.position.x) * lerp * delta;
        camera.position.y += (playerPos.y - camera.position.y) * lerp * delta;

        float vw = camera.viewportWidth * camera.zoom;
        float vh = camera.viewportHeight * camera.zoom;

        // clamp the camera position to the size of the map
        camera.position.x = MathUtils.clamp(camera.position.x, vw / 2f, mapWidth * PPM / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, vh / 2f, mapHeight * PPM / 2f);

        camera.update();

        world.step(delta, 6,2);

        player.update(delta);


    }

    @Override
    public void resize(int width, int height) {
        // camera.setToOrtho(false,(float)width/16,(float)height/16);
        vfxManager.resize(width, height);
        viewport.update(width, height);
        hud.getStage().getViewport().update(width, height);
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
        vfxManager.dispose();
        effectBloom.dispose();
        effectTv.dispose();
        effectDistortion.dispose();
        effectVignetting.dispose();
        effectFxaa.dispose();

        batch.dispose();
        fontBatch.dispose();
        font.dispose();
        world.dispose();
        b2dr.dispose();
        mapLoader.dispose();
        player.dispose();
        for (College college : colleges) {
            college.dispose();
        }
    }
}
