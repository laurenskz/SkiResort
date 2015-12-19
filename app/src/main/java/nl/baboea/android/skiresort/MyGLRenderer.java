package nl.baboea.android.skiresort;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;

import javax.microedition.khronos.opengles.GL10;

import nl.baboea.android.skiresort.game.Game;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 13-9-2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private float zRot;
    private Game game;
    private TexturedSquare b;
    private long time;
    private Context context;

    public MyGLRenderer(Context context){
        this.context = context;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        time = System.currentTimeMillis();
        game = new Game(context);
    }

    public void onDrawFrame(GL10 unused) {
        long newTime = System.currentTimeMillis();
        long milliSecondsPassed = newTime - time;
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        if (game != null) {
            game.update(milliSecondsPassed);
            game.draw();
        }

        time = newTime;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
