package com.boatcorp.boatgame.tools;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import org.graalvm.compiler.asm.aarch64.AArch64Assembler;

public class B2dSteeringEntity implements Steerable<Vector2> {

    Body body;
    Boolean tagged;
    float boundingRadius;
    float maxLinearVelocity,maxLinearAcceleration;
    float maxAngularVelocity,maxAngularAcceleration;
    float ZeroLinearSpeedThreshold;

    /**
     * Sets the behaviour for the AI entity to tell it how to move and interact
     */
    SteeringBehavior<Vector2> behavior;
    /**
     * Output from this can be applied to the box2D body to steer in the intended direction
     */
    SteeringAcceleration<Vector2> steeringOutput;

    public B2dSteeringEntity(Body body,float boundingRadius){
        this.body = body;
        this.boundingRadius = boundingRadius;

        maxLinearVelocity = 100;
        maxLinearAcceleration = 5000;
        maxAngularVelocity = 30;
        maxAngularAcceleration = 5;
        ZeroLinearSpeedThreshold = 0.01f;

        tagged = false;

        steeringOutput = new SteeringAcceleration<>(new Vector2());

    }

    public Body getBody(){
        return body;
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior){
        this.behavior = behavior;
    }

    public SteeringBehavior<Vector2> getBehavior(){
        return behavior;
    }

    public void update(float delta){
        if (behavior != null){
            behavior.calculateSteering(steeringOutput);
            applySteering(delta);
        }
    }

    private void applySteering(float delta){
        boolean anyAcceleration = false;

        if (!steeringOutput.linear.isZero()){
            Vector2 force = steeringOutput.linear.scl(1/delta);
            body.applyForceToCenter(force,true);
            anyAcceleration = true;
        }

        if (steeringOutput.angular !=0){
            body.applyTorque(steeringOutput.angular * (1/delta), true);
            anyAcceleration = true;
        }else{
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero()){
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * 1/delta);
                body.setTransform(body.getPosition(),newOrientation);
            }
        }

        if (anyAcceleration){
            Vector2 vel = body.getLinearVelocity();
            //gets single magnitude value of speed,
            float speed = vel.len2();
            if (speed > Math.pow(maxLinearVelocity,2)){
                //sets current speed to max linear speed
                System.out.println(vel);
                body.setLinearVelocity(vel.scl(maxLinearVelocity/ (float) Math.sqrt(speed)));
            }

            if (body.getAngularVelocity() > maxAngularVelocity){
                body.setAngularVelocity(maxAngularVelocity);
            }
        }
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        System.out.println("bound");
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }


    @Override
    public float getMaxLinearSpeed() {
        return maxLinearVelocity;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearVelocity = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularVelocity;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularVelocity = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x,vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }


    @Override
    public Location<Vector2> newLocation() {
        return new EntityLocation();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return ZeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.ZeroLinearSpeedThreshold = value;
    }

    @Override
    public void setOrientation(float orientation) {
        System.out.println("her");
    }

}
