package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.entities.College;
import com.boatcorp.boatgame.entities.Player;
import com.boatcorp.boatgame.frameworks.Hud;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.tools.MapLoader;
import com.boatcorp.boatgame.tools.WorldContactListener;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.boatcorp.boatgame.GameState;

import java.util.ArrayList;
import java.util.Random;

import static com.boatcorp.boatgame.screens.Constants.*;

public class PlayScreen implements Screen {

    //---------------
    private boolean ENABLE_SHADERS = true;
    private boolean BOX2D_WIREFRAME = true;
    //---------------


    private final BoatGame boatGame;
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
    private Box2DDebugRenderer debugRenderer;

    // For Shader
    private VfxManager vfxManager;
    private BloomEffect effectBloom;
    private OldTvEffect effectTv;
    private RadialDistortionEffect effectDistortion;
    private VignettingEffect effectVignetting;
    private FxaaEffect effectFxaa;


    public PlayScreen(BoatGame game, GameState state) {
        this.boatGame = game;
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        world = new World(GRAVITY, true);
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640 / PPM, 480 / PPM, camera);
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

        mapLoader = new MapLoader();
        player = new Player(viewport,world,state);
        colleges = new ArrayList<>();
        colleges.add(new College("langwith", world,state));
        colleges.add(new College("james", world, state));
        colleges.add(new College("goodricke", world,state));
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
        hud = new Hud(fontBatch, player);
        PointSystem.setPoints(state.points);
        System.out.println(state.collegePositions.get("james").x);

        world.setContactListener(new WorldContactListener(this));

        addWorldBorder();

        // Create shaders
        effectTv = new OldTvEffect();
        effectVignetting = new VignettingEffect(false);
        effectDistortion = new RadialDistortionEffect();
        effectBloom = new BloomEffect();
        effectFxaa = new FxaaEffect();

        // Configure shaders
        effectTv.setTime(0.2f);
        effectVignetting.setIntensity(0.4f);
        effectVignetting.setSaturation(0.2f);
        effectDistortion.setDistortion(0.1f);

        // Add shaders to manager, order matters
        vfxManager.addEffect(effectTv);
        vfxManager.addEffect(effectDistortion);
        vfxManager.addEffect(effectBloom);
        vfxManager.addEffect(effectVignetting);
        vfxManager.addEffect(effectFxaa);

        //resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //Box2D debug renderer
        debugRenderer = new Box2DDebugRenderer(BOX2D_WIREFRAME,false,false,false,BOX2D_WIREFRAME,BOX2D_WIREFRAME);
    }
    private void addWorldBorder(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0,0);
        Body worldBorder = world.createBody(bodyDef);

        //Left side of world
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0,1371);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        worldBorder.createFixture(fixtureDef);

        //Bottom side of world
        shape.setAsBox(1421,0);
        fixtureDef.shape = shape;
        worldBorder.createFixture(fixtureDef);

        //Top side of world
        shape.setAsBox(1421,0,new Vector2(1421,1371),0);
        fixtureDef.shape = shape;
        worldBorder.createFixture(fixtureDef);

        //Right side of world
        shape.setAsBox(0,1371,new Vector2(1421,1371),0);
        fixtureDef.shape = shape;
        worldBorder.createFixture(fixtureDef);

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
        for (College college : colleges) { //TODO this really needs rethinking.
            college.setMatrix(camera.combined);
        }
        batch.setProjectionMatrix(camera.combined);
        b2dr.render(world, camera.combined);
        mapLoader.render(camera);

        for (College college : colleges) {
            college.draw();
        }
        player.draw();

        //Draws box2D hitboxes for debug
        debugRenderer.render(world, viewport.getCamera().combined);

        fontBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.setPointScore("Points: " + PointSystem.getPoints());
        hud.setHealthValue(player.getHealth());
        hud.getStage().draw();

        hud.getStage().act(delta);

        combat(delta);

        vfxManager.endInputCapture();

        if (ENABLE_SHADERS) {
            vfxManager.applyEffects();
        }

        vfxManager.renderToScreen((Gdx.graphics.getWidth() - viewport.getScreenWidth())/2,
                (Gdx.graphics.getHeight() - viewport.getScreenHeight())/2,
                viewport.getScreenWidth(), viewport.getScreenHeight());




    }

    //TODO rename this, maybe implement directly into act/render method
    private void combat(float delta) {
        if (player.isDead()) {
            player.dispose();
            for(College college : colleges) {
                college.dispose();
            }
            boatGame.setScreen(new ResultScreen(false, boatGame));
        }
        if (colleges.isEmpty()) {
            boatGame.setScreen(new ResultScreen(true, boatGame));
        }
        for (int i = 0; i < colleges.size(); i++) {
            College college = colleges.get(i);
            if (college.isAlive()) {
                college.combat(camera.combined, player,delta);
            } else {
                college.dispose();
                colleges.remove(college);
                PointSystem.incrementPoint(500);
            }
        }
        player.combat(camera.combined, colleges,delta);
    }

    private void update(final float delta) {
        
        boolean save = Gdx.input.isKeyJustPressed(Input.Keys.C);
        boolean load = Gdx.input.isKeyJustPressed(Input.Keys.V);

        if(save){boatGame.saveGame();}
        if(load){boatGame.loadGame();}
        
        camera.zoom = DEFAULT_ZOOM;

        // Get properties of the map from the TileMap
        MapProperties prop = mapLoader.getMap().getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);

        // Using `lerping` to slightly lag camera behind player //TODO modify this, player gets too close to edge of screen
        float lerp = 10f;
        Vector2 playerPos = player.getPosition();
        Vector2 playerSprite = player.getSpriteDimensions();
        camera.position.x += ((playerPos.x) - camera.position.x) * lerp * delta;
        camera.position.y += ((playerPos.y) - camera.position.y) * lerp * delta;


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
        //camera.setToOrtho(false,(float)width/16,(float)height/16);

        viewport.update(width, height);
        vfxManager.resize(viewport.getScreenWidth(), viewport.getScreenHeight());

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
        batch.dispose();
        hud.dispose();
        fontBatch.dispose();
        font.dispose();
        b2dr.dispose();
        mapLoader.dispose();
        player.dispose();
        for (College college : colleges) {
            college.dispose();
        }
        world.dispose();
        vfxManager.dispose();
        effectTv.dispose();
        effectDistortion.dispose();
        effectVignetting.dispose();
        effectBloom.dispose();
        effectFxaa.dispose();
    }
    
    public GameState getState(){
        GameState state = new GameState();
        Random rand = new Random();
        state.playerPosition = player.getPosition();
        state.currentHealth = player.getHealth();
        state.maxHealth = player.getMaxHealth();
        state.points = PointSystem.getPoints();
        state.collegeHealths.clear();
        state.collegePositions.clear();
        for (College college : colleges) {
            float[] healths = {college.getCurrentHealth(), college.getMaxHealth()};
            state.collegeHealths.put(college.getUserData(),healths);
            state.collegePositions.put(college.getUserData(), college.getPosition());
        }
        return state;
    }
}

