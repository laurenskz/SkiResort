package nl.baboea.android.skiresort.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import nl.baboea.android.skiresort.AbstractShape;
import nl.baboea.android.skiresort.Camera;
import nl.baboea.android.skiresort.Input;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.Square;
import nl.baboea.android.skiresort.Text;
import nl.baboea.android.skiresort.game.model.SharedGameModel;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 18-9-2015.
 */
public class Game {

    public static final String HIGHSCORE_PREFIX = "Highscore : ";
    private Activity context;
    public static final String TAG = "Game";
    private ArrayList<GameParticipant> participants = new ArrayList<>();
    private ArrayList<ParticipantContainer> containers = new ArrayList<>();
    private HashMap<Class, AbstractShape> bounds = new HashMap<>();
    private Player player;
    private EnvironmentSpawner environmentSpanwer;
    private Text scoreText;
    private Text gameOver = new Text("Game over");
    private Text tapToRespawn = new Text("Tap anywhere to restart");
    private Text highScoreText = new Text("");
    private boolean released = false;//This is for at the end of a game, a player has to release the screen and then tap it again.

    public Game(Activity context) {
        this.context = context;
        initializeGame();
        Model neutral = new Model();
        for(GameParticipant participant : participants){
            if(bounds.get(participant.getClass())!=null)continue;
            bounds.put(participant.getClass(), participant.getShape().getBounds(neutral));
        }
        gameOver.getModel().setPosition(new Vec3(-0.75f, 0.8f, -1f));
        gameOver.getModel().setScale(new Vec3(1.5f, 1.5f, 1.5f));
        tapToRespawn.getModel().setPosition(new Vec3(-0.8f, -0.6f, -1f));
        tapToRespawn.getModel().setScale(new Vec3(0.5f,0.5f,0.5f));
        highScoreText.getModel().setPosition(new Vec3(-0.8f,0f,-1f));
    }

    private void initializeGame(){
        player = new Player();
        Camera.setPosition(player.getModel().getPosition());
        populate();
        emptyContainers();//Background should be added first for smooth blending of pixels (because player is the top no other textures will pas the depth test and if player has transparent pixels clear color will be visible)
        participants.add(player);
        scoreText = new Text("0");
        scoreText.getModel().setPosition(new Vec3(-0.9f, 1f, -1f));
        released = false;
    }

    private void restart(){
        clearGame();
        initializeGame();
    }

    private int getHighScore(){
        SharedPreferences myPrefs;
        myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        return myPrefs.getInt("highScore", 0);
    }

    private int setAndGetHighScore(int newScore){
        int old = getHighScore();
        if(newScore<=old)return old;
        SharedPreferences prefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("highScore", newScore);
        editor.commit();
        toast("New highscore!", Toast.LENGTH_SHORT);
        return newScore;
    }


    private void toast(final String message, final int length){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, length).show();
            }
        });
    }
    private void clearGame(){
        participants.clear();
        containers.clear();
    }

    private void emptyContainers(){
        for(ParticipantContainer container : containers){
            participants.addAll(container.getAll());
        }
    }

    public void draw() {
        for(GameParticipant participant : participants){
            //Log.d(TAG, "draw " + participant.toString());
            if(participant.isInView()){
                participant.draw();
                if(bounds.get(participant.getClass())==null)continue;
                //bounds.get(participant.getClass()).draw(participant.getModel());//Turn this on to draw the bounds
            }
        }
        scoreText.draw();
        if(player.hasCrashed()){
            drawCrashTexts();
        }
    }

    public void update(long milliSecondsPassedSinceLastFrame){
        crashRoutine();//This will only activate if the player has crashed
        cleanUp();//Clean up our memory a little bit. Also makes collision detection less costly
        float secondsPassed = (float)milliSecondsPassedSinceLastFrame/1000;
        for(GameParticipant participant : participants){
            participant.update(secondsPassed);
            if(participant!=player&&!player.hasCrashed()){
                if(participant.playerHasPassed())continue;//This will make our game more efficent.
                if(!participant.isInView())continue;
                if(participant.needsCollisionDetection()){
                    if(participant.collidesWith(player)){
                        boolean crash = participant.onCollisionWithPlayer(player);
                        if(crash){
                            int newScore = player.crash();
                            int highScore = setAndGetHighScore(newScore);
                            highScoreText.setText(HIGHSCORE_PREFIX+highScore);
                        }
                    }
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
        int score = player.getScore();
        scoreText.setText(String.valueOf(score));
        SharedGameModel.getInstance().draw();
        Camera.update();//This always has to be done because we want to keep an eye at the player
    }

    private void crashRoutine(){
        if(player.hasCrashed()){
            if(released){
                if(Input.pressed)restart();
            }else{
                if(!Input.pressed)released = true;
            }
        }
    }

    private void drawCrashTexts(){
        gameOver.draw();
        tapToRespawn.draw();
        highScoreText.draw();
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
