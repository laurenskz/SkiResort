package nl.baboea.android.skiresort.game;

import android.util.Log;

import java.util.ArrayList;

import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;
import static nl.baboea.android.skiresort.game.StaticRandom.*;
/**
 * Created by Laurens on 5-12-2015.
 */
public class GenerationTileFiller {

    public static final String TAG = "GenerationTileFiller";
    public static final int AMOUNT_OF_STARTING_POINTS = 3;
    public static final float FORBIDDEN_WIDTH = 2f;
    private ArrayList<Spawner> spawners = new ArrayList<>();
    private float spawnerTotal;
    private Vec3 point;//This point is important as it is the point we want to generate
    private float width;
    private float height;
    private ArrayList<GameParticipant> generatedParticipants = new ArrayList<>();
    private ArrayList<Vec3> generatedPoints = new ArrayList<>();
    public static final int AMOUNT_OF_GRID_POINTS = 7;
    private ArrayList<Vec3> startingSafePoints;

    public GenerationTileFiller(Vec3 point, float width, float height) {
        this.point = point;
        this.width = width;
        this.height = height;
        populateSpawners();
        initStartingSafePoints();
    }

    private void initStartingSafePoints(){
        startingSafePoints = new ArrayList<>();
        float widthBetweenPoints = this.width/(AMOUNT_OF_STARTING_POINTS+1);
        for(int i = 1 ; i <= AMOUNT_OF_STARTING_POINTS;i++){
            Vec3 startingPoint = new Vec3(i*widthBetweenPoints,0,0);
            startingSafePoints.add(startingPoint);
        }
    }


    public void setPoint(Vec3 point) {
        this.point = point;
    }

    public void generate(){
        generatedParticipants.clear();//Clean up some things from the last run.
        generatedPoints.clear();
        if(point==null)return;//Safety check
        generatedPoints.add(point);
        makeModels();
    }

    private void makeModels(){
        int amountOfModels = random.nextInt(10)+ 4;
        ArrayList<Vec3> safePoints = getSafeSkeleton();
        for(int i = 0 ; i < amountOfModels ; i++){
            GameParticipant gameParticipant = newModel();
            setRandomPoint(safePoints,gameParticipant.getModel().getPosition());
            generatedParticipants.add(gameParticipant);
        }
    }

    private void setRandomPoint(Vec3 toSet){
        float x = (float) randDouble(point.getX(),point.getX()+width);
        float y = (float) randDouble(point.getY(),point.getY()-height);
        toSet.setX(x);
        toSet.setY(y);
    }

    private void setRandomPoint(ArrayList<Vec3> safePoints,Vec3 toSet){
        float y = (float) randDouble(0,height);
        float x = newX(safePoints,y);
        if(x<0){
            toSet.setX(0);
            toSet.setY(0);
            return;
        }
        toSet.setX(point.getX()+x);//X can only be positive but that's okay since point.x = the left bound
        toSet.setY(point.getY()-y);//Y is lower than the point
    }

    /**
     * This method looks for the points in between which this y value is
     * @param safePoints
     * @param y
     * @return
     */
    private float[] xValuesOnLine(ArrayList<Vec3> safePoints, float y){
        if(y<safePoints.get(0).getY()){//This is between the 3 starting points and the first real point.
            float[] xValues = new float[startingSafePoints.size()];
            int i = 0;
            for(Vec3 startingSafePoint : startingSafePoints){
                xValues[i++] = xValueOfLineOn(y,startingSafePoint,safePoints.get(0));
            }
            return xValues;
        }
        Log.d(TAG, "xValuesOnLine y = " + y);
        int i;
        for(i = 0 ; i < safePoints.size();i++){
            Log.d(TAG, "xValuesOnLine safePoints["+i+"]"+"="+safePoints.get(i));
            if(safePoints.get(i).getY()<=y&&safePoints.get(i+1).getY()>=y)break;//We have found a winner
        }
        return new float[]{xValueOfLineOn(y-safePoints.get(i).getY(),safePoints.get(i),safePoints.get(i+1))};
    }

    /**
     * Generating a random float within a range excluding subranges, based on http://stackoverflow.com/questions/25891033/get-a-random-float-within-range-excluding-sub-range
     * @param forbidden
     * @return
     */
    private float newX(float[] forbidden){
        float halfWidth = FORBIDDEN_WIDTH/2;
        float random = (float)randDouble(0,width-(FORBIDDEN_WIDTH*forbidden.length));
        for(float forbid : forbidden){
            if(random>forbid-halfWidth){
                random+=FORBIDDEN_WIDTH;
            }
        }
        return random;
    }

    private float newX(ArrayList<Vec3> safePoints, float y){
        float[] forbiddenPoints = xValuesOnLine(safePoints,y);
        return newX(forbiddenPoints);
    }

    /**
     * This method expects two to be the lower point and one to be the higher.
     * @param y
     * @param one
     * @param two
     * @return
     */
    private float xValueOfLineOn(float y, Vec3 one,Vec3 two){
        float dy = two.getY()-one.getY();
        float dx = two.getX()-one.getX();
        float xPerY = dx/dy;
        return y*xPerY+one.getX();//Add it to the starting point
    }


    /**
     * This creates the skeleton of points in which no objects can spawn, these points are "connected" by lines creating a safe line for the
     * player to go through
     * @return
     */
    private ArrayList<Vec3> getSafeSkeleton(){
        ArrayList<Vec3> toRet = new ArrayList<>();
        float incrementer = this.height/ AMOUNT_OF_GRID_POINTS;//What we add each loop
        float height = incrementer;//Skip this because we want special first points
        while(height<this.height+0.1f){//Add a little marge because we actually want to include the last point
            Vec3 newPoint = new Vec3((float)randDouble(0,this.width),height,0);
            toRet.add(newPoint);
            height+=incrementer;
        }
        return toRet;
    }

    private GameParticipant newModel(){
        float diceRoll = (float)randDouble(0,spawnerTotal);
        float start = 0;
        for(Spawner spawner : spawners){
            start += spawner.getRatio();
            if(diceRoll<=start){
                return spawner.getInstance();
            }
        }
        if(spawners.size()>=1){//Why not
            return spawners.get(0).getInstance();
        }
        return null;
    }

    public ArrayList<GameParticipant> getAll(){
        return new ArrayList<GameParticipant>(generatedParticipants);
    }


    private void populateSpawners(){
        spawners.add(Tree.getSpawner());//Add the trees
        setSpawnerTotal();
    }

    private void setSpawnerTotal(){
        float total = 0;
        for(Spawner spawner : spawners){
            total += spawner.getRatio();
        }
        spawnerTotal = total;
    }

    public ArrayList<Vec3> getAllGeneratedGridPoints(){
        return new ArrayList<Vec3>(generatedPoints);
    }
}
