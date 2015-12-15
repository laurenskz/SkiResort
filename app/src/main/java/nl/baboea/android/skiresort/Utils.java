package nl.baboea.android.skiresort;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Laurens on 13-9-2015.
 */
public class Utils {


    public static final String UTILS = "Utils";
    private static ArrayList<Integer> texIDs = new ArrayList<>();

    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) {
        String ret = null;
        try{
            ret = convertStreamToString(MainActivity.assetManager.open(filePath));
            //Make sure you close all streams.
        }catch(Exception e) {
            Log.d(UTILS, e.toString());
        }
        return ret;
    }

}
