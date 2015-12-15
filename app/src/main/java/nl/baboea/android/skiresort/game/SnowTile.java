package nl.baboea.android.skiresort.game;

import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 19-11-2015.
 */
public class SnowTile extends MultiTextured {

    private static final int[] TEXTURES = new int[]{R.drawable.snow};
    private Model model = new Model();

    public SnowTile(){
        super(TEXTURES);
    }


    @Override
    Model getModel() {
        return model;
    }

    @Override
    void update(float secondsPassed) {
        //We will be updated by our container(Background)
    }


}
