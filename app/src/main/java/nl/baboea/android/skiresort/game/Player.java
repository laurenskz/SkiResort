package nl.baboea.android.skiresort.game;

import android.opengl.GLES20;
import android.util.Log;

import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Input;
import nl.baboea.android.skiresort.MainActivity;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 14-11-2015.
 */
public class Player extends MultiTextured {


    private static final float SCALE = 0.35f;
    public static final int[] TEXTURES = new int[]{R.drawable.santa_straight, R.drawable.santa_left, R.drawable.santa_right};
    public static final float START_SPEED = 2f;

    private enum Direction {STRAIGHT, LEFT, RIGHT};
    private Model model;
    private float speed;
    private boolean crashed = false;


    public Player(){
        super(TEXTURES);
        speed = START_SPEED;
        model = new Model();
        model.setScale(new Vec3(SCALE,SCALE,SCALE));
        selected = Direction.STRAIGHT;
        model.getPosition().setZ(-0.05f);
        model.getPosition().setY(1);//Start a little above zero.
    }


    @Override
    public Model getModel() {
        return model;
    }



    @Override
    public void update(float secondsPassed) {
        if(crashed)return;
        if(!Input.pressed){
            selected = Direction.STRAIGHT;
        }else{
            float halfWidth = (float)Constants.WIDTH/2;
            float halfHeight = (float)Constants.HEIGHT/2;
            if(Input.lastX>halfWidth)selected = Direction.RIGHT;
            if(Input.lastX<halfWidth)selected = Direction.LEFT;
        }
        basicMove(secondsPassed);
    }

    public int crash(){
        crashed = true;
        return getScore();
    }

    public boolean hasCrashed() {
        return crashed;
    }

    public int getScore(){
        int toRet = (int) -(getModel().getPosition().getY());
        return toRet;
    }
    public int getScore(boolean log){
        int toRet = (int) -(getModel().getPosition().getY());
//        Log.d(TAG, "getScore " + getModel().getPosition());
//        Log.d(TAG, "getScore " + toRet);
        return toRet;
    }

    private void basicMove(float secondsPassed){
        if(Input.pressed){
            float halfWidth = (float)Constants.WIDTH/2;
            boolean right = Input.lastX>halfWidth;
            float x = right ? 1*speed : -1*speed;
            getModel().getPosition().incrementX(x*secondsPassed);
        }
        float y = Input.pressed ?  (float)(-speed * secondsPassed*0.63f) :-speed*secondsPassed;
        getModel().getPosition().incrementY(y);
        speed+=0.01*secondsPassed;

    }
}
