package nl.baboea.android.skiresort;

import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Mat4f;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 14-9-2015.
 */
public class Camera {

    private static Mat4f viewMatrix = Mat4f.identity();
    private static Vec3 position;
    private static final float X_OFFSET = 0f, Y_OFFSET = 0.9f, Z_OFFSET = 0f;


    public static Mat4f getViewMatrix() {
        return viewMatrix;
    }

    public static void update(){
        viewMatrix = Mat4f.translation(-getPosition().getX(),-getPosition().getY(),0f);//We don't want to follow the z position
        //viewMatrix.scale(0.1f,0.1f,1f);
    }

    public static Vec3 getPosition(){
        return new Vec3(position.getX()-X_OFFSET,position.getY()-Y_OFFSET,0f);
    }

    public static void setPosition(Vec3 position) {
        Camera.position = position;
    }

}
