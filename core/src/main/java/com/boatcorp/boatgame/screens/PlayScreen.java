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
    private final ArrayList<EnemyShip> enemyShips;
    private final ArrayList<SeaMonster> seaMonsters;
    private final Hud hud;
    private final Box2DDebugRenderer debugRenderer;
    private final Stage gameStage;
    private final GameState state;
    private ArrayList<Bullet> globalBullets = new ArrayList<>();



    // For Shader
    private final VfxManager vfxManager;
    private final BloomEffect effectBloom;
    private final OldTvEffect effectTv;
    private final RadialDistortionEffect effectDistortion;
    private final VignettingEffect effectVignetting;
    private final FxaaEffect effectFxaa;

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
        hasBoughtGreen = state.hasBoughtGreen;
        hasBoughtRed = state.hasBoughtRed;
        hasBoughtHealth = state.hasBoughtHealth;
        if (!state.isSpawn){timeSinceFreeze = TimeUtils.millis() + state.timeSinceFreeze;}
        isFrozen = state.isFrozen;

        mapLoader = new MapLoader();
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
                System.out.println(state.monsterStartPositions);
                SeaMonster tempSeaMonster =  new SeaMonster(world,player,state,0);
                gameStage.addActor(tempSeaMonster);
                seaMonsters.add(tempSeaMonster);
            }
        }



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
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

        //hud.getStage().act(delta); //Don't think this line is needed as well as the draw function

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
                PointSystem.incrementPoint(500);
                float doubleRandomNumber = (float) Math.random() * 50;
                PlunderSystem.incrementPlunder(doubleRandomNumber + 50);
            }
        }

        //Logic for enemy ship combat and death
        for (EnemyShip ship: enemyShips){
            if (ship.isAlive()){
                globalBullets.addAll(ship.shoot());
            } else{
                ship.dispose();
                PointSystem.incrementPoint(100);
                float doubleRandomNumber = (float) Math.random() * 10;
                PlunderSystem.incrementPlunder(doubleRandomNumber + 10);
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


        Integer ids = -1;
        for (SeaMonster monster: seaMonsters){
            if (!monster.isAlive()){
                monster.dispose();
                toRemoveMonster.add(monster);
                ids = monster.getId();
            }
        }
        if (ids != -1){
            state.monsterPositions = new ArrayList<>();
            state.monsterHealths= new ArrayList<>();
            state.monsterStartPositions= new ArrayList<>();
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

        // Get properties of the map from the TileMap
        MapProperties prop = mapLoader.getMap().getProperties();
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);

        // Using `lerping` to slightly lag camera behind player //TODO modify this, player gets too close to edge of screen
        float lerp = 10f;
        Vector2 playerPos = player.getPosition();
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
                state.shipTimes.add(0l);
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
        if (health && hasBoughtHealth){
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

    public void loadBullets(){


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
        player.updateState();
        for (College college : colleges) {
            college.updateState();
        }
        for (Bullet bullet : globalBullets){
            bullet.updateState();
        }

        for (EnemyShip ship : enemyShips){
            ship.updateState();
        }

        for (SeaMonster monster : seaMonsters){
            monster.updateState();
        }

        state.isSpawn = false;
        state.shopUnlocked = shopUnlocked;
        state.hasBoughtRed = hasBoughtRed;
        state.hasBoughtHealth = hasBoughtHealth;
        state.hasBoughtGreen = hasBoughtGreen;
        state.shipDamageScaler = enemyShips.get(0).getDamageScaler();
        state.isFrozen =  isFrozen;
        state.timeSinceFreeze = TimeUtils.timeSinceMillis(timeSinceUpdate);
        return state;
    }
}


