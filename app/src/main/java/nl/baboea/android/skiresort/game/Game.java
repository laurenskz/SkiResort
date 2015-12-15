package nl.baboea.android.skiresort.game;

import android.opengl.GLES20;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import nl.baboea.android.skiresort.AbstractShape;
import nl.baboea.android.skiresort.Camera;
import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Input;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;
import nl.baboea.android.skiresort.Square;
import nl.baboea.android.skiresort.Texture;
import nl.baboea.android.skiresort.TexturedSquare;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 18-9-2015.
 */
public class Game {

    public static final String TAG = "Game";
    private ArrayList<GameParticipant> participants = new ArrayList<>();
    private ArrayList<ParticipantContainer> containers = new ArrayList<>();
    private HashMap<Class, AbstractShape> bounds = new HashMap<>();
    private Player player;
    private Square square = new Square(EnvironmentSpawner.GENERATION_WIDTH,EnvironmentSpawner.GENERATION_HEIGHT);
    private EnvironmentSpawner environmentSpanwer;
    private Square screen;

    public Game() {
        player = new Player();
        Camera.setPosition(player.getModel().getPosition());
        square.getModel().setPosition(Camera.getPosition());
        populate();
        emptyContainers();//Background should be added first for smooth blending of pixels (because player is the top no other textures will pas the depth test and if player has transparent pixels clear color will be visible)
        participants.add(player);
        Model neutral = new Model();
        for(GameParticipant participant : participants){
            if(bounds.get(participant.getClass())!=null)continue;
            bounds.put(participant.getClass(), participant.getShape().getBounds(neutral));
        }
    }

    private void emptyContainers(){
        for(ParticipantContainer container : containers){
            participants.addAll(container.getAll());
        }
    }

    public void draw() {
        for(GameParticipant participant : participants){
            //Log.d(TAG, "draw " + participant.toString());
            //if(participant.isInView()){
                participant.draw();
                //bounds.get(participant.getClass()).draw(participant.getModel());
            //}
        }
//        for(Vec3 v :environmentSpanwer.getPointsAtGenerated()){
//            Vec3 vec = new Vec3(v);
//            vec.setZ(-1f);
//            square.setPosition(vec);
//            square.draw();
//        }
//        Vec3 camPos = Camera.getPosition();
//        camPos.incrementX(-1);//Now we are to the left
//        camPos.incrementY((1f / Constants.RATIO));
//        camPos.setZ(-1f);
//        if(screen==null){
//            screen = new Square(2f,2*(1f/Constants.RATIO));
//        }
//        screen.setPosition(camPos);
//        screen.draw();
////        player.draw();
////        square.draw();
    }

    public void update(long milliSecondsPassedSinceLastFrame){
        cleanUp();//Clean up our memory a little bit. Also makes collision detection less costly
        float secondsPassed = (float)milliSecondsPassedSinceLastFrame/1000;
        for(GameParticipant participant : participants){
            participant.update(secondsPassed);
            if(participant!=player){
                if(participant.playerHasPassed())continue;//This will make our game more efficent.
                if(participant.collidesWith(player)){
                    participant.onCollisionWithPlayer(player);
                }
                //Log.d(TAG, "update participant passOffset = " + participant.getPassOffset());
                if(participant.getModel().getPosition().getY()+participant.getPassOffset()>player.getModel().getPosition().getY()){
                    participant.playerPassed(player);
                }
            }
        }
        for(ParticipantContainer container : containers){
            GameParticipant[] array = container.update(secondsPassed);
            if(array!=null) Collections.addAll(participants,array);
        }
        Camera.update();//This always has to be done because we want to keep an eye at the player

    }

    private void cleanUp(){
        for(Iterator<GameParticipant> iterator = participants.iterator() ;iterator.hasNext() ;){
            GameParticipant participant = iterator.next();
            if(participant instanceof SnowTile)continue;
            if(participant.isDeletable())iterator.remove();
        }
    }

    private void populate(){
        containers.add(new Background());
        environmentSpanwer = new EnvironmentSpawner();
        containers.add(environmentSpanwer);
    }
}
