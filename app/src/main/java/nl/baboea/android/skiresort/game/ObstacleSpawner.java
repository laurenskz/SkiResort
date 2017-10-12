package nl.baboea.android.skiresort.game;


import nl.baboea.android.skiresort.R;

/**
 * Created by Laurens on 15-11-2016.
 */
public class ObstacleSpawner implements Spawner {

    private static final int[][] ids = new int[][]{
            new int [] {R.drawable.house,R.drawable.house_collision},
            new int [] {R.drawable.rendier,R.drawable.rendier_collision},
            new int [] {R.drawable.snowman,R.drawable.snowman_collision}
    };
    private static final float[] scales = new float[]{0.9f,0.5f,1.2f};

    @Override
    public GameParticipant getInstance() {
        int id = StaticRandom.random.nextInt(ids.length);
        Obstacle obstacle = new Obstacle(ids[id]);
        obstacle.getModel().getScale().scale(scales[id]);
        obstacle.getModel().getPosition().setZ(-0.06f);
        return obstacle;
    }
    @Override
    public float getRatio() {
        return 10;
    }
}
