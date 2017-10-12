package nl.baboea.android.skiresort.game;

import android.util.Log;

import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.Model;
import nl.baboea.android.skiresort.R;
import nl.baboea.android.skiresort.Text;
import nl.baboea.android.skiresort.Texture;
import nl.baboea.android.skiresort.TexturedSquare;
import nl.baboea.android.skiresort.Utils;
import nl.baboea.android.skiresort.game.messages.Message;
import nl.baboea.android.skiresort.game.model.SharedGameModel;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 15-11-2016.
 */
public class Present extends MultiTextured {

    private static Message current;
    private Model model = new Model();

    static {
        String stringFromFile = Utils.getStringFromFile("fun/quotes.txt");
        split = stringFromFile.split("\n");
    }

    private static String[] split;

    public Present() {
        super(new int[]{R.drawable.present});
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
        deletable = true;
        if(current!=null)current.setValid(false);
        Message message = new Message(split[StaticRandom.random.nextInt(split.length)]);
        message.setTtl(1000);
        message.getText().getModel().setPosition(new Vec3(-0.75f, 0.8f, -1f));
        SharedGameModel.getInstance().add(message);
        current = message;
        return false;
    }

    public static Spawner getSpawner(){
        return new Spawner() {
            @Override
            public GameParticipant getInstance() {
                Present toRet = new Present();
                toRet.getModel().getScale().scale(0.5f);
                toRet.getModel().getPosition().setZ(-0.06f);//Player is -0.05f and we have to be less
                return toRet;
            }

            @Override
            public float getRatio() {
                return Constants.PRESENT_RATIO;
            }
        };
    }
}
