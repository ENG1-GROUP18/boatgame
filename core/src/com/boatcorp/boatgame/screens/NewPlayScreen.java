package com.boatcorp.boatgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.boatcorp.boatgame.BoatGame;
import com.boatcorp.boatgame.entities.NewCollege;
import com.boatcorp.boatgame.entities.NewPlayer;

public class NewPlayScreen implements Screen {

    //TODO notes:
    // batch might not be needed, stage has its own.


    BoatGame game;
    Stage stage;
    World world;
    Box2DDebugRenderer debugRenderer;

    NewPlayer player;

    Camera camera;
    Viewport viewport;


    public NewPlayScreen(BoatGame game) {
        this.game = game;


        camera = new OrthographicCamera(1,1);
        viewport = new FitViewport(1280/4f,720/4f, camera);
        stage = new Stage(viewport);

        world = new World(new Vector2(0,0),true);
        debugRenderer = new Box2DDebugRenderer(true, false, false, false, true, true);

        player = new NewPlayer(world, this);
        stage.addActor(player);

        stage.addActor(new NewCollege(world,40,20));

    }

    @Override
    public void render(float delta) {
        // Clean up buffers
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(delta,3,3);
        stage.act();

        camera.position.x = player.getPosition().x;
        camera.position.y = player.getPosition().y;

        stage.draw();
        debugRenderer.render(world, stage.getCamera().combined);

    }




    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public World getWorld(){
        return world;
    }
}
