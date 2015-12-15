package nl.baboea.android.skiresort.game;

import java.util.Random;

/**
 * Created by Laurens on 21-11-2015.
 */
public class StaticRandom {

    public static Random random = new Random();

    public static double randDouble(double min, double max){
        double randomValue = min + (max - min) * random.nextDouble();
        return randomValue;
    }


}
