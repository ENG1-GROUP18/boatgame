package com.boatcorp.boatgame.screens;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.entities.Bullet;
import com.boatcorp.boatgame.entities.College;
import com.boatcorp.boatgame.entities.EnemyShip;
import com.boatcorp.boatgame.entities.Player;
import com.boatcorp.boatgame.frameworks.Hud;
import com.boatcorp.boatgame.frameworks.PlunderSystem;
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
    private ArrayList<EnemyShip> enemyShips;
    private final Hud hud;
    private Box2DDebugRenderer debugRenderer;
    private Stage gameStage;
    private GameState state;
    private ArrayList<ArrayList<Bullet>> bulletsC = new ArrayList<>();


    // For Shader
    private VfxManager vfxManager;
    private BloomEffect effectBloom;
    private OldTvEffect effectTv;
    private RadialDistortionEffect effectDistortion;
    private VignettingEffect effectVignetting;
    private FxaaEffect effectFxaa;

    //For hud updates
    private boolean hudUpdateNeeded;
    private long timeSinceUpdate;

    //For shop
    private boolean shopUnlocked;

    public PlayScreen(BoatGame game, GameState state) {
        this.boatGame = game;
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        world = new World(GRAVITY, true);
        b2dr = new Box2DDebugRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(640 / PPM, 480 / PPM, camera);
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        gameStage = new Stage(viewport);
        this.state = state;
        shopUnlocked = state.shopUnlocked;

        mapLoader = new MapLoader();
        player = new Player(world,state);
        colleges = new ArrayList<>();
        enemyShips = new ArrayList<>();

        if (state.isSpawn){setMode(state.difficulty);}
        addColleges(colleges);
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
        hud = new Hud(fontBatch, player);
        PointSystem.setPoints(state.points);
        PlunderSystem.setPlunder(state.plunder);
        if (shopUnlocked) {hud.setShopLabel("press M for the shop");}
        
        world.setContactListener(new WorldContactListener(this));
        gameStage.addActor(player);


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
        debugRenderer = new Box2DDebugRenderer(true,false,false,false,true,true);
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
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();

        update(delta);

        gameStage.act();

        // Batch drawing
        for (College college : colleges) { //TODO this really needs rethinking.
            college.setMatrix(camera.combined);
        }
        batch.setProjectionMatrix(camera.combined);
        b2dr.render(world, camera.combined);
        mapLoader.render(camera);

        for (College college : colleges) {
            college.draw();
        }
        gameStage.draw();


        fontBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.setPointScore("Points: " + PointSystem.getPoints());
        hud.setHealthValue(player.getHealth());
        hud.setPlunderScore("Plunder: " + PlunderSystem.getPlunder());


        hud.getStage().draw();

        hud.getStage().act(delta);

        //Draws box2D hitboxes for debug
        if (boatGame.ENABLE_BOX2D_WIREFRAME) {
            debugRenderer.render(world, viewport.getCamera().combined);
        }

        combat(delta);
        vfxManager.endInputCapture();

        if (boatGame.ENABLE_SHADERS) {
            vfxManager.applyEffects();
        }

        vfxManager.renderToScreen((Gdx.graphics.getWidth() - viewport.getScreenWidth())/2,
                (Gdx.graphics.getHeight() - viewport.getScreenHeight())/2,
                viewport.getScreenWidth(), viewport.getScreenHeight());

    }

    //TODO rename this, maybe implement directly into act/render method
    private void combat(float delta) {
        ArrayList<String> toRemoveName = new ArrayList<>();
        ArrayList<College> toRemoveCollage = new ArrayList<>();
        ArrayList<EnemyShip> toRemoveShip = new ArrayList<>();
        for (College college : colleges) {
            if (college.isAlive()) {
                college.combat(camera.combined, player,delta);
            }
            else {
                upgradePlayer(6 - colleges.size());
                toRemoveName.add(college.getUserData().toString());
                state.collegeHealths.remove(college.getUserData().toString());
                state.collegePositions.remove(college.getUserData().toString());
                toRemoveCollage.add(college);
                college.dispose();
                PointSystem.incrementPoint(500);
                float doubleRandomNumber = (float) Math.random() * 50;
                PlunderSystem.incrementPlunder(doubleRandomNumber + 50);
            }
        }

        ArrayList<Bullet> bulletsP;
        bulletsP = player.combat(colleges,enemyShips);
        if (!bulletsP.isEmpty()){
            batch.begin();
            for (Bullet bullet: bulletsP) {
                // Draw and move bullets
                bullet.draw(batch,1);
                bullet.move(delta);
            }
            batch.end();
        }

        int monoid = 0;
        for (EnemyShip ship: enemyShips){
            if (ship.isAlive()){
                if (monoid == 0){
                    bulletsC = new ArrayList<>();
                    monoid = 1;
                }
                bulletsC.add(ship.shoot(delta));

            } else{
                ship.dispose();
                toRemoveShip.add(ship);
            }
        }
        ArrayList<Bullet> toRemoveBullet = new ArrayList<>();
        if (!bulletsC.isEmpty()) {
            batch.begin();
        for (ArrayList<Bullet> temp: bulletsC){
            for (Bullet bullet : temp) {
                // Draw and move bullets
                if (!bullet.outOfRange(300)) {

                    bullet.draw(batch, 1);
                    bullet.move(delta);
                } else {
                    toRemoveBullet.add(bullet);
                }

            }
        }
            batch.end();
        }
        bulletsC.removeAll(toRemoveBullet);

        for (Actor actor: gameStage.getActors()){
            if (actor.getX() < 0 && actor.getY() < 0){
                actor.remove();
            }
        }

        state.collegeNames.removeAll(toRemoveName);
        colleges.removeAll(toRemoveCollage);
        enemyShips.removeAll(toRemoveShip);





        if (player.isDead()) {
            player.dispose();
            for (Actor actor: gameStage.getActors()){
                actor.addAction(Actions.removeActor());

            }
            for(College college : colleges) {
                college.dispose();
            }
            boatGame.setScreen(new ResultScreen(false, boatGame));
        }
        if (colleges.isEmpty()) {
            boatGame.setScreen(new ResultScreen(true, boatGame));
        }


    }

    private void update(final float delta) {

        boolean save1 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_1);
        boolean save2 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_2);
        boolean save3 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_3);
        boolean save4 = Gdx.input.isKeyJustPressed(Input.Keys.NUM_4);
        boolean pause = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
        boolean shop = Gdx.input.isKeyJustPressed(Input.Keys.M);

        if(save1){boatGame.saveGame("1");}
        if(save2){boatGame.saveGame("2");}
        if(save3){boatGame.saveGame("3");}
        if(save4){boatGame.saveGame("4");}
        if(pause){pause();}
        if(shop && shopUnlocked){
            boatGame.setScreen(new ShopScreen(boatGame, getState()));
        }

        if(hudUpdateNeeded && (TimeUtils.timeSinceMillis(timeSinceUpdate) > 4000)){
            hud.setUpdateAlert("");
            hudUpdateNeeded = false;
        }
        
        camera.zoom = DEFAULT_ZOOM;

        // TODO this really shouldn't be here, no need to get this every update
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



    }

    @Override
    public void resize(int width, int height) {
        //camera.setToOrtho(false,(float)width/16,(float)height/16);

        viewport.update(width, height);
        vfxManager.resize(viewport.getScreenWidth(), viewport.getScreenHeight());

        hud.getStage().getViewport().update(width, height);
    }

    public void pause() {
        boatGame.setScreen(new PauseScreen(boatGame));
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


    public void addColleges(ArrayList<College> colleges) {

        Random rand = new Random();
        int divider = state.collegeNames.size() / 2;
        int xUnit = 1200 / divider;
        for (int i = 0; i < state.collegeNames.size(); i++) {
            if (state.isSpawn){
                state.collegeHealths.put(state.collegeNames.get(i), state.collegeHealth);
                if( i < divider){
                    state.collegePositions.put(state.collegeNames.get(i), new Vector2((xUnit*(i)) + rand.nextInt(xUnit), rand.nextInt(600)));

                }
                else{
                    state.collegePositions.put(state.collegeNames.get(i), new Vector2((xUnit*(i%divider)) + rand.nextInt(xUnit), 600 + rand.nextInt(600)));
                }}
            colleges.add(new College(state.collegeNames.get(i), world,state));
        }

        //Place enemy ships at collages
        for (College college : colleges) {
            enemyShips.add((new EnemyShip(world, state, "1",
                    new Vector2(college.getPosition().x - 40, college.getPosition().y - 40), player, camera.combined)));
            gameStage.addActor(enemyShips.get(enemyShips.size() - 1));

        }
    }


    public void setMode(int mode){
        switch(mode){
            case 0:
                player.scaleDamage(0.7f);
                for (College college : this.colleges){
                    college.scaleDamage(1.3f);}
                break;
            case 1:
                break;
            case 2:
                player.scaleDamage(1.3f);
                for (College college : this.colleges){
                    college.scaleDamage(0.7f);}
                break;
        }
    }
    /**
     * Gives the player a power-up, like immunity or increased health
     * @param type Picks which power-up to apply: 0. Damage Increase, 1.Full health, 2. Immunity
     */
    public void upgradePlayer(int type){
        switch(type){
            case 0:
                hud.setUpdateAlert("Powerup! \nYou just unlocked\nthe shop");
                hud.setShopLabel("press M for the shop");
                shopUnlocked = true;
                timeSinceUpdate = TimeUtils.millis();
                hudUpdateNeeded = true;
                break;
            case 1:
                player.scaleDamage(0.8f);
                hud.setUpdateAlert("Powerup! \nYour armour just\nimproved 25%");
                timeSinceUpdate = TimeUtils.millis();
                hudUpdateNeeded = true;
                break;
            case 2:
                //TODO: one shot kill OR freeze enemies
                break;
            case 3:
                player.setImmuneSeconds(20);
                hud.setUpdateAlert("Powerup! \nYou just won 20\nseconds immunity");
                timeSinceUpdate = TimeUtils.millis();
                hudUpdateNeeded = true;
                break;
            case 4:
                player.setHealth(player.getMaxHealth());
                hud.setUpdateAlert("Powerup! \nYour just won a\nhealth refill");
                timeSinceUpdate = TimeUtils.millis();
                hudUpdateNeeded = true;
                break;

        }
    }

    /**
     * Gives the player a useful macro when purchased in the shop
     * @param macro Picks which macro to apply
     */
    public void purchaseMacro(String macro){
        switch(macro){
            case "R":
                player.setRedMacro();
                break;
            case "G":
                player.setGreenMacro();
                break;
            case "H":
                player.setHealthMacro();
                break;

        }
    }



    
    public GameState getState(){
        player.updateState();
        for (College college : colleges) {
            college.updateState();
        }
        state.isSpawn = false;
        state.shopUnlocked = shopUnlocked;
        return state;
    }
}





