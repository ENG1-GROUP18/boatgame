package com.boatcorp.boatgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.boatcorp.boatgame.GameState;
import com.boatcorp.boatgame.tools.B2dSteeringEntity;

import java.util.ArrayList;

public class EnemyShip extends Group {
    private final Sprite sprite;
    private final Vector2 position;
    private float health = 20;
    private final Body body;
    private final Body startBody;
    private final World gameWorld;
    private B2dSteeringEntity entity,targetPlayer,targetHome;
    private Arrive<Vector2> arriveToPlayer,arriveToStartPos;
    private ArrayList<Bullet> bullets;
    private Matrix4 camera;
    private Player player;
    private GameState gameState;
    private FiniteState currentState;
    private long timeSinceLastShot;

    //TODO simplify/cleanup so not so many objects are passed in
    public EnemyShip(World world, GameState state, String ID, Vector2 position,Player player,Matrix4 camera){
        Texture texture = new Texture(Gdx.files.internal("Entities/boat2.png"));
        sprite = new Sprite(texture);
        gameWorld = world;
        gameState = state;
        this.player = player;
        this.camera = camera;
        timeSinceLastShot = TimeUtils.millis();

        this.position = position;
        bullets = new ArrayList<>();

        //Creates body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.fixedRotation = true;
        body = gameWorld.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/4, sprite.getHeight()/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef).setUserData("EnemyShip");
        body.setUserData("");
        body.setLinearDamping(3);

        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set(position);
        startBody = gameWorld.createBody(bodyDef1);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape;
        fixtureDef1.isSensor = true;
        startBody.createFixture(fixtureDef1);


        //Ship AI behaviour
        entity = new B2dSteeringEntity(body,10);
        targetPlayer = new B2dSteeringEntity(player.getBody(),10);
        targetHome = new B2dSteeringEntity(startBody,10);

        currentState = FiniteState.STAY;
        arriveToPlayer = new Arrive<>(entity,targetPlayer)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(50)
                .setDecelerationRadius(10);

        entity.setBehavior(arriveToPlayer);

        arriveToStartPos = new Arrive<>(entity,targetHome)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(0)
                .setDecelerationRadius(10);


        this.addActor(new Image(sprite));
        this.setPosition(position.x,position.y);
        this.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float dist = (float) Math.hypot(targetPlayer.getBody().getPosition().y-entity.getBody().getPosition().y,
                targetPlayer.getBody().getPosition().x-entity.getBody().getPosition().x);

        float distanceFromHome = (float) Math.hypot(startBody.getPosition().y-entity.getBody().getPosition().y,
                startBody.getPosition().x-entity.getBody().getPosition().x);

        //Logic on what current state the enemy ship is in
        if (dist < 150 && distanceFromHome < 300 && currentState != FiniteState.RETURN){
            entity.setBehavior(arriveToPlayer); //TODO change so only sets on state change, could cause lag otherwise
            currentState = FiniteState.FOLLOW;
            entity.update(delta);
        } else if (!(entity.getLinearVelocity().isZero(0.01f))){
            currentState = FiniteState.RETURN;
            entity.setBehavior(arriveToStartPos);
            entity.update(delta);

        } else{
            currentState = FiniteState.STAY;
            entity.getBody().setLinearVelocity(0,0);
            entity.getBody().setAngularVelocity(0);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setPosition(body.getPosition().x-(sprite.getWidth()/2), body.getPosition().y-(sprite.getHeight()/2));
        this.setRotation((float)Math.toDegrees(body.getAngle()));
        super.draw(batch, parentAlpha);
    }

    //TODO change so it renders using stage2D
    public void shoot(float delta){

        ArrayList<Bullet> toRemove = new ArrayList<>();
        if (currentState == FiniteState.FOLLOW){
            // Only begins combat when the player is close enough and the college isn't defeated

            if ((TimeUtils.timeSinceMillis(timeSinceLastShot)) > 1000) {
                timeSinceLastShot = TimeUtils.millis();

                Vector2 velocity = new Vector2();
                velocity.x =(float) -Math.sin(entity.getOrientation());
                velocity.y =(float) Math.cos(entity.getOrientation());
                bullets.add(new Bullet(body.getPosition(), velocity, gameWorld, "EnemyShip","bullet"));

            }
            for (Bullet bullet: bullets) {
                // Draw and move bullets and check for collisions
                bullet.setMatrix(camera);
                bullet.draws();
                bullet.move(delta);
                if (bullet.outOfRange(300)) {
                    bullet.dispose();
                    toRemove.add(bullet);
                }
                if (player.isHit() && bullet.hit()) {
                    bullet.dispose();
                    toRemove.add(bullet);
                    player.takeDamage(10);
                }
            }

        } else {
            if (!bullets.isEmpty()) {
                for (Bullet bullet: bullets) {
                    bullet.setMatrix(camera);
                    bullet.draws();
                    bullet.move(delta);
                    if (bullet.outOfRange(300)) {
                        bullet.dispose();
                        toRemove.add(bullet);
                    }
                }
                for (Bullet bullet : bullets) {
                    if (bullet.outOfRange(300)) {
                        //bullet.dispose();
                        toRemove.add(bullet);
                    }
                }
            }
        }
        bullets.removeAll(toRemove);
    }

    private enum FiniteState {
        FOLLOW,
        RETURN,
        STAY
    }

    public boolean isHit(){
        if (body.getUserData() == "Hit"){
            return true;
        } else{
            return false;
        }
    }

    public void takeDamage(int damage){
        if(health > 0){
            health -=damage;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String getState() {
        return currentState.toString();
    }

    public void dispose() {
        this.setPosition(-100,-100);
        gameWorld.destroyBody(body);
        if (!bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                bullet.dispose();
            }
        }
    }
}
