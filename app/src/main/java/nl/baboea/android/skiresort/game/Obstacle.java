package nl.baboea.android.skiresort.game;

import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;

/**
 * Created by Laurens on 15-11-2016.
 */
public class Obstacle extends MultiTextured {

    private Model model = new Model();

    public Obstacle(int id){
        super(new int[]{id},"Obstacle"+id);
        setPassOffSetPercentages(85f);
        needsCollisionDetection = true;
    }

    public Obstacle(int[] ids) {
        super(ids,true,"Obstacle"+ids[0]);
        setPassOffSetPercentages(85f);
        needsCollisionDetection = true;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void update(float secondsPassed) {

    }

    @Override
    public void playerPassed(Player player) {
        super.playerPassed(player);
        playerPassed = true;
    }

    @Override
    public boolean onCollisionWithPlayer(Player player) {
        if(playerPassed)return false;
        return true;
    }
}
