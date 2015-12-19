package nl.baboea.android.skiresort.game;

import android.util.Log;

import nl.baboea.android.skiresort.AbstractShape;
import nl.baboea.android.skiresort.Camera;
import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 18-9-2015.
 */
public abstract class GameParticipant {

    public static final String TAG = "GameParticipant";

    abstract Model getModel();
    abstract AbstractShape getShape();
    abstract void update(float secondsPassed);
    protected boolean deletable = false;
    protected boolean playerPassed = false;
    protected boolean needsCollisionDetection = false;
    protected float passOffset = 0f;

    public boolean collidesWith(GameParticipant other){
        return getShape().collidesWith(other.getShape(),getModel(),other.getModel());
    }


    public void draw(){
        getShape().draw(getModel());
    }

    public boolean isDeletable() {
        if(deletable)return true;
        Vec3 camera = Camera.getPosition();
        Vec3 position = getModel().getPosition();
        if(position.getY()<camera.getY())return false;
        float dy = Math.abs(camera.getY()-position.getY());
        return dy-getShape().getHeight()/2>(1.3*(1/Constants.RATIO));//Add a little marge just to be safe
    }

    public boolean needsCollisionDetection() {
        return needsCollisionDetection;
    }

    public float getPassOffset(){
        return passOffset;
    }

    /**
     * This sets the pass offset. This is the amount the player has to have passed the participant to be considered beneath it.
     * @param percentage should be between 0 and 100
     */
    public void setPassOffSetPercentages(float percentage){
        if(percentage<0)return;
        if(percentage>100)return;
        if(percentage==50)passOffset = 0;
        float height = getShape().getHeight();
        height*=getModel().getScale().getY();
        if(percentage<50){
            passOffset = (1-(percentage/100))*height;
        }else{
            passOffset = -(((percentage-50)/100)*height);
            //Log.d(TAG, "setPassOffSetPercentages passOffset = " + passOffset);
        }
    }

    public boolean isInView(){
        Vec3 camera = Camera.getPosition();
        Vec3 position = getModel().getPosition();
        float dx = camera.getX()-position.getX();
        float dy = camera.getY()-position.getY();
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        dx-=getShape().getWidth()/2;
        dy-=getShape().getHeight()/2;
        return dx<1&&dy<(1/ Constants.RATIO);
    }

    public void playerPassed(Player player){
        getModel().getPosition().setZ(-0.04f);//Can be overridden but we don't want things to appear in front of player when we've passed it
        playerPassed = true;
    }

    public boolean playerHasPassed(){
        return playerPassed;
    }

    /**
     *
     * @param player
     * @return true if the player should be dead now, else return false
     */

    public boolean onCollisionWithPlayer(Player player){
        return false;
    }

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return "["+name + "@" + getModel().getPosition().toString()+"]";
    }
}
