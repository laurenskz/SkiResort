package nl.baboea.android.skiresort.game;

import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;

/**
 * Created by Laurens on 5-12-2015.
 */
public class Tree extends MultiTextured{

    private Model model = new Model();
    private static int[] TEXTURES = new int[] {R.drawable.christmas_tree,R.drawable.christmas_tree_collision};

    public Tree(){
        super(TEXTURES,true);
        setPassOffSetPercentages(85f);
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
    public void onCollisionWithPlayer(Player player) {
        if(playerPassed)return;
        this.deletable = true;
    }

    public static Spawner getSpawner(){
        return new Spawner() {
            @Override
            public GameParticipant getInstance() {
                Tree toRet = new Tree();
                toRet.getModel().getPosition().setZ(-0.06f);//Player is -0.05f and we have to be less
                return toRet;
            }

            @Override
            public float getRatio() {
                return Constants.TREE_RATIO;
            }
        };
    }

}
