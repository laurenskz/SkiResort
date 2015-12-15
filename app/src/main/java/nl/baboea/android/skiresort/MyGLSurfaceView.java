package nl.baboea.android.skiresort;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Mat4f;

/**
 * Created by Laurens on 13-9-2015.
 */
public class MyGLSurfaceView extends GLSurfaceView {


    private boolean done;
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer();
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Constants.HEIGHT = getHeight();
        Constants.WIDTH = getWidth();
        Constants.RATIO = (float)getWidth()/getHeight();
        Mat4f.loadUpProjectionMatrix();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if(action==MotionEvent.ACTION_UP){
            Input.pressed = false;
            return false;
        }
        Input.lastX = event.getX();
        Input.lastY = event.getY();
        Input.pressed = true;
        return true;
    }
}
