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
import com.boatcorp.boatgame.entities.*;
import com.boatcorp.boatgame.frameworks.Hud;
import com.boatcorp.boatgame.frameworks.PlunderSystem;
import com.boatcorp.boatgame.frameworks.PointSystem;
import com.boatcorp.boatgame.tools.MapLoader;
import com.boatcorp.boatgame.tools.WorldContactListener;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.*;
import com.boatcorp.boatgame.GameState;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Random;


/**
 * Contains all the functions necessary to run the game and displays it on the screen.
 */
public class PlayScreen implements Screen {

    /** The object which holds the data for the game*/
    private final BoatGame boatGame;
    /** A batch to draw images onto the screen */
    private final SpriteBatch batch;
    /** A front batch to draw the HUD onto the screen */
    private final SpriteBatch fontBatch;
    /** A world object which holds data about the box2D world */
    private final World world;
    /** The camera object which is used to set the relative position of objects on the screen*/
    private final OrthographicCamera camera;
    /** The viewport object which determines how world coordinates are mapped */
    private final Viewport viewport;
    /** Loads and renders the map*/
    private  MapLoader mapLoader;
    /** An object to hold the font for the game*/
    private final BitmapFont font;
    /** The player object which takes user inputs to interact with the game*/
    private final Player player;
    /** An arraylist to hold all the currently rendered collages*/
    private final ArrayList<College> colleges;
    /** An arraylist to hold all the currently rendered enemyShips*/
    private final ArrayList<EnemyShip> enemyShips;
    /** An arraylist to hold all the currently rendered seaMonsters*/
    private final ArrayList<SeaMonster> seaMonsters;
    /** A hud object to display information on the screen for the player*/
    private final Hud hud;
    /** A debug renderer for BOX2d to show hit-boxes during development*/
    private  Box2DDebugRenderer debugRenderer;
    /** A game stage where the game objects are rendered on in a specific order*/
    private final Stage gameStage;
    /** A game state which contains the information about the game objects on load*/
    private final GameState state;
    /** An arraylist to hold all the current bullets on screen*/
    private ArrayList<Bullet> globalBullets = new ArrayList<>();



    // For Shader
    /** */
    private  VfxManager vfxManager;
    private  BloomEffect effectBloom;
    private  OldTvEffect effectTv;
    private  RadialDistortionEffect effectDistortion;
    private  VignettingEffect effectVignetting;
    private  FxaaEffect effectFxaa;

    // for freeze
    private long timeSinceFreeze;
    private boolean isFrozen;

    //For hud updates
    private boolean hudUpdateNeeded;
    private long timeSinceUpdate;

    //For shop
    private boolean shopUnlocked;
    private final boolean hasBoughtGreen;
    private final boolean hasBoughtRed;
    private final boolean hasBoughtHealth;

    //Constants
    public static final float PPM = 25f;
    public static final Vector2 GRAVITY = new Vector2(0,0);
    public static final float DEFAULT_ZOOM = 16f;

    public PlayScreen(BoatGame game, GameState state) {
        this.boatGame = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(640 / PPM, 480 / PPM, camera);
        //Sets up sprite batches for testing or non-testing
        if (boatGame.HEADLESS){
            batch = Mockito.mock(SpriteBatch.class);
            fontBatch = Mockito.mock(SpriteBatch.class);
            gameStage = new Stage(viewport,Mockito.mock(SpriteBatch.class));
        }  else {
            batch = new SpriteBatch();
            fontBatch = new SpriteBatch();
            gameStage = new Stage(viewport);
            mapLoader = new MapLoader();

        }


        world = new World(GRAVITY, true);


        this.state = state;
        shopUnlocked = state.shopUnlocked;
        hasBoughtGreen = state.hasBoughtGreen;
        hasBoughtRed = state.hasBoughtRed;
        hasBoughtHealth = state.hasBoughtHealth;
        if (!state.isSpawn){timeSinceFreeze = TimeUtils.millis() + state.timeSinceFreeze;}
        isFrozen = state.isFrozen;


        player = new Player(world,state);
        colleges = new ArrayList<>();
        enemyShips = new ArrayList<>();
        seaMonsters = new ArrayList<>();

        if (state.isSpawn){setMode(state.difficulty);}
        addColleges();
        addBullets();
        font = new BitmapFont(Gdx.files.internal("fonts/korg.fnt"), Gdx.files.internal("fonts/korg.png"), false);
        hud = new Hud(fontBatch, player);
        PointSystem.setPoints(state.points);
        PlunderSystem.setPlunder(state.plunder);
        if (shopUnlocked) {hud.setShopLabel("press M for the shop");}

        world.setContactListener(new WorldContactListener(this));
        gameStage.addActor(player);


        //TODO make the sea monsters spawn in different locations - currently this is just lazily implemented
        if (state.isSpawn){

            state.monsterPositions.add(new Vector2(200,250));
            state.monsterStartPositions.add(new Vector2(200,250));
            state.monsterHealths.add(40f);
            SeaMonster tempSeaMonster =  new SeaMonster(world,player,state,0);
            gameStage.addActor(tempSeaMonster);
            seaMonsters.add(tempSeaMonster);
        } else {
            if (!state.monsterStartPositions.isEmpty()){
                SeaMonster tempSeaMonster =  new SeaMonster(world,player,state,0);
                gameStage.addActor(tempSeaMonster);
                seaMonsters.add(tempSeaMonster);
            }
        }



        addWorldBorder();


        // Create shaders
        if (!boatGame.HEADLESS) {
            vfxManager = new VfxManager(Pixmap.Format.RGBA8888);

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
            debugRenderer = new Box2DDebugRenderer(true, false, false, false, true, true);
        }
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
        if (!boatGame.HEADLESS){
            vfxManager.cleanUpBuffers();
            vfxManager.beginInputCapture();
        }


        update(delta);

        gameStage.act();

        // Batch drawing
        for (College college : colleges) { //TODO this really needs rethinking.
            college.setMatrix(camera.combined);
        }
        batch.setProjectionMatrix(camera.combined);
        if (!boatGame.HEADLESS) {
            mapLoader.render(camera);
            gameStage.draw();
        }

        for (College college : colleges) {
            college.draw();
        }



        fontBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.setPointScore("Points: " + PointSystem.getPoints());
        hud.setHealthValue(player.getHealth());
        hud.setPlunderScore("Plunder: " + PlunderSystem.getPlunder());

        if (!boatGame.HEADLESS) {
            hud.getStage().draw();
        }


        //hud.getStage().act(delta); //Don't think this line is needed as well as the draw function

        combat(delta);

        //Draws box2D hitboxes for debug
        if (!boatGame.HEADLESS) {
            if (boatGame.ENABLE_BOX2D_WIREFRAME) {
                debugRenderer.render(world, viewport.getCamera().combined);
                vfxManager.endInputCapture();
            }

            if (boatGame.ENABLE_SHADERS) {
                vfxManager.applyEffects();
            }

            vfxManager.renderToScreen((Gdx.graphics.getWidth() - viewport.getScreenWidth()) / 2,
                    (Gdx.graphics.getHeight() - viewport.getScreenHeight()) / 2,
                    viewport.getScreenWidth(), viewport.getScreenHeight());
        }
    }

    //TODO rename this, maybe implement directly into act/render method
    private void combat(float delta) {
        ArrayList<String> toRemoveName = new ArrayList<>();
        ArrayList<College> toRemoveCollage = new ArrayList<>();
        ArrayList<EnemyShip> toRemoveShip = new ArrayList<>();
        ArrayList<SeaMonster> toRemoveMonster = new ArrayList<>();
        //Logic for collage combat and capture
        for (College college : colleges) {
            if (college.isAlive()) {

                globalBullets.addAll(college.combat(player));
            }
            else {
                upgradePlayer(6 - colleges.size());
                toRemoveName.add(college.getUserData().toString());
                state.collegeHealths.remove(college.getUserData().toString());
                state.collegePositions.remove(college.getUserData().toString());
                toRemoveCollage.add(college);
                college.dispose();
                if (boatGame.difficulty == 0){
                    PointSystem.incrementPoint(100);
                    float doubleRandomNumber = (float) Math.random() * 100;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 100);}
                if (boatGame.difficulty == 1){
                    PointSystem.incrementPoint(250);
                    float doubleRandomNumber = (float) Math.random() * 50;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 50);}
                if (boatGame.difficulty == 2){
                    PointSystem.incrementPoint(500);
                    float doubleRandomNumber = (float) Math.random() * 25;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 25);}
            }
        }

        //Logic for enemy ship combat and death
        for (EnemyShip ship: enemyShips){
            if (ship.isAlive()){
                globalBullets.addAll(ship.shoot());
            } else{
                ship.dispose();
                if (boatGame.difficulty == 0){
                    PointSystem.incrementPoint(25);
                    float doubleRandomNumber = (float) Math.random() * 25;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 25);}
                if (boatGame.difficulty == 1){
                    PointSystem.incrementPoint(50);
                    float doubleRandomNumber = (float) Math.random() * 10;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 10);}
                if (boatGame.difficulty == 2){
                    PointSystem.incrementPoint(100);
                    float doubleRandomNumber = (float) Math.random() * 0;
                    PlunderSystem.incrementPlunder(doubleRandomNumber + 0);}
                toRemoveShip.add(ship);
            }
        }

        //Adds player bullets to array
        globalBullets.addAll(player.combat());

        //handles bullets
        handleBullets();

        //Renders all the bullets in a single sprite batch
        batch.begin();
        ArrayList<Bullet> toRemoveBullet = new ArrayList<>();
        for (Bullet bullet : globalBullets) {
            if (!bullet.outOfRange(300)) {
                bullet.draw(batch, 1);
                bullet.move(delta);
            } else {
                toRemoveBullet.add(bullet);
            }
        }
        globalBullets.removeAll(toRemoveBullet);

        batch.end();


        for (SeaMonster monster: seaMonsters){
            if (!monster.isAlive()){
                monster.dispose();
                toRemoveMonster.add(monster);
            }
        }




        //Removes actors which have been moved out of the game space
        for (Actor actor: gameStage.getActors()){
            if (actor.getX() < 0 && actor.getY() < 0){
                actor.remove();
            }
        }

        state.collegeNames.removeAll(toRemoveName);
        colleges.removeAll(toRemoveCollage);
        enemyShips.removeAll(toRemoveShip);
        seaMonsters.removeAll(toRemoveMonster);

        //Lose state if player dies
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
        //Win state if all collages are captured
        if (colleges.isEmpty()) {
            boatGame.setScreen(new ResultScreen(true, boatGame));
        }


    }

    private void update(final float delta) {

        if (isFrozen && (TimeUtils.timeSinceMillis(timeSinceUpdate) > 20000)){
            unfreeze();
        }

        handleMacros();

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



        // Using `lerping` to slightly lag camera behind player //TODO modify this, player gets too close to edge of screen
        float lerp = 10f;
        Vector2 playerPos = player.getPosition();
        camera.position.x += ((playerPos.x) - camera.position.x) * lerp * delta;
        camera.position.y += ((playerPos.y) - camera.position.y) * lerp * delta;


        float vw = camera.viewportWidth * camera.zoom;
        float vh = camera.viewportHeight * camera.zoom;


        if (!boatGame.HEADLESS) {
            // Get properties of the map from the TileMap
            MapProperties prop = mapLoader.getMap().getProperties();
            int mapWidth = prop.get("width", Integer.class);
            int mapHeight = prop.get("height", Integer.class);

            // clamp the camera position to the size of the map
            camera.position.x = MathUtils.clamp(camera.position.x, vw / 2f, mapWidth * PPM / 2f);
            camera.position.y = MathUtils.clamp(camera.position.y, vh / 2f, mapHeight * PPM / 2f);
        }
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
        boatGame.setScreen(new PauseScreen(boatGame, getState()));
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
        if (!state.headless){
            fontBatch.dispose();
            vfxManager.dispose();
            effectTv.dispose();
            effectDistortion.dispose();
            effectVignetting.dispose();
            effectBloom.dispose();
            effectFxaa.dispose();
            debugRenderer.dispose();
            mapLoader.dispose();
        }
        font.dispose();

        player.dispose();
        for (College college : colleges) {
            college.dispose();
        }
        world.dispose();

    }


    public void addColleges() {
        Random rand = new Random();
        int divider = state.collegeNames.size() / 2;
        int xUnit = (divider > 0) ? (1200 / divider) : 1;
        int buffer = 120;
        for (int i = 0; i < state.collegeNames.size(); i++) {
            if (state.isSpawn){
                state.collegeHealths.put(state.collegeNames.get(i), state.collegeHealth);
                if (i < divider){
                    state.collegePositions.put(state.collegeNames.get(i), new Vector2((xUnit*(i)) + buffer + rand.nextInt(xUnit - (2*buffer)), buffer + rand.nextInt(600 - (2*buffer))));
                }
                else{
                    state.collegePositions.put(state.collegeNames.get(i), new Vector2((xUnit*(i%divider)) + buffer + rand.nextInt(xUnit - (2*buffer)), 600 + buffer + rand.nextInt(600 - (2*buffer))));
                }
            }

            colleges.add(new College(state.collegeNames.get(i), world,state));
        }

        //Place enemy ships at collages
        Vector2 tempEnemyShipPosition;
        if (state.isSpawn){
            for (int i = 0; i < state.collegeNames.size(); i++) {
                tempEnemyShipPosition = new Vector2(colleges.get(i).getPosition().x - 40, colleges.get(i).getPosition().y - 40);
                state.shipPositions.add(tempEnemyShipPosition);
                state.shipStartPositions.add(tempEnemyShipPosition);
                state.shipHealths.add(20f);
                state.shipTimes.add(0L);
                enemyShips.add(new EnemyShip(world,state,player,i));
                gameStage.addActor(enemyShips.get(enemyShips.size() - 1));
            }
        } else  {
            for (int i = 0; i < state.shipPositions.size(); i++) {
                enemyShips.add(new EnemyShip(world,state,player,i));
                gameStage.addActor(enemyShips.get(enemyShips.size() - 1));
            }
        }
    }
    /**
    * Sets the difficulty mode of the game
    @param mode Picks which mode to play in: 0. Easy 1. Normal 2. Hard
    */

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
                freeze();
                timeSinceFreeze = TimeUtils.timeSinceMillis(timeSinceFreeze);
                hud.setUpdateAlert("Powerup! \nYour enemies just\nfroze!");
                timeSinceUpdate = TimeUtils.millis();
                hudUpdateNeeded = true;
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
     * Used for testing to get info about the player
     * @return the current player object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Used for testing to get info about the enemy ships
     * @return the array of current enemy ships
     */
    public ArrayList<EnemyShip> getEnemyShips(){
        return enemyShips;
    }

    public void handleMacros(){
        boolean red = Gdx.input.isKeyPressed(Input.Keys.R);
        boolean green = Gdx.input.isKeyPressed(Input.Keys.G);
        boolean health = Gdx.input.isKeyPressed(Input.Keys.H);
        String bulletColor = player.getBulletColor();
        if (red && hasBoughtRed){
            if (bulletColor.equals("bullet") || bulletColor.equals("greenbullet")){
                player.setBulletColor("redbullet");
                scaleShips(2);
            }
            else{
                player.setBulletColor("bullet");
                scaleShips(1/2f);
            }
        }
        if (green && hasBoughtGreen){
            if (bulletColor.equals("bullet") || bulletColor.equals("redbullet") ){
                player.setBulletColor("greenbullet");
            }
            else{
                player.setBulletColor("bullet");
            }

        }
        if (health && hasBoughtHealth && (player.getHealth() != player.getMaxHealth())){
            if (PlunderSystem.getPlunder() > 50){
                PlunderSystem.decrementPlunder(50);
                player.setHealth(player.getMaxHealth());

            }

        }


    }

    private void scaleShips(float scaler){
        for (EnemyShip ship : enemyShips){
            ship.scaleDamage(scaler);
        }
    }


    public void freeze(){
        for (SeaMonster seaMonster : seaMonsters){
            seaMonster.freeze();
        }
        for (EnemyShip enemyShip : enemyShips){
            enemyShip.freeze();
        }
        isFrozen = true;
    }
    public void unfreeze(){
        for (SeaMonster seaMonster : seaMonsters){
            seaMonster.unfreeze();
        }
        for (EnemyShip enemyShip : enemyShips){
            enemyShip.unfreeze();
        }
        isFrozen = false;
    }

    public void handleBullets(){
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet: globalBullets) {
            // Move bullets and check for collisions
            if (player.isHit() && bullet.hit()) {
                bullet.dispose();
                toRemove.add(bullet);
                player.takeDamage(10);
            }
            if (bullet.outOfRange(300)) {
                    bullet.dispose();
                    toRemove.add(bullet);
                }
            for (College college : colleges) {
                if (college.isHit() && bullet.hit()) {
                    bullet.dispose();
                    toRemove.add(bullet);
                    college.takeDamage(5);
            }
            }
            for (EnemyShip ship : enemyShips){
                if (ship.isHit() && bullet.hit()){
                    bullet.dispose();
                    toRemove.add(bullet);
                    ship.takeDamage(5);
            }
        }
            for (SeaMonster monster : seaMonsters){
                if (monster.isHit() && bullet.hit()){
                    bullet.dispose();
                    toRemove.add(bullet);
                //If green upgrade has been bought then it increases damage to sea monster
                if (player.getBulletColor().equals("greenbullet")){
                    monster.takeDamage(10);
                }else{
                    monster.takeDamage(5);
                }

            }
        }
        }
        globalBullets.removeAll(toRemove);

    }

    public void addBullets(){
        for (int i = 0; i < state.firedFroms.size(); i++){
            globalBullets.add(new Bullet(state.positions.get(i), state.velocities.get(i), world, state.firedFroms.get(i), state.bulletColors.get(i), state));
        }
    }

    public GameState getState(){
        //Create new state to overwrite the old one
        GameState newState = new GameState();
        newState.collegeNames = new ArrayList<>();
        player.updateState(newState);
        for (College college : colleges) {
            college.updateState(newState);
        }
        for (Bullet bullet : globalBullets){
            bullet.updateState(newState);
        }

        for (EnemyShip ship : enemyShips){
            ship.updateState(newState);
        }

        for (SeaMonster monster : seaMonsters){
            monster.updateState(newState);
        }

        newState.isSpawn = false;
        newState.shopUnlocked = shopUnlocked;
        newState.hasBoughtRed = hasBoughtRed;
        newState.hasBoughtHealth = hasBoughtHealth;
        newState.hasBoughtGreen = hasBoughtGreen;
        newState.shipDamageScaler = enemyShips.get(0).getDamageScaler();
        newState.isFrozen =  isFrozen;
        newState.timeSinceFreeze = TimeUtils.timeSinceMillis(timeSinceUpdate);
        return newState;
    }
}


