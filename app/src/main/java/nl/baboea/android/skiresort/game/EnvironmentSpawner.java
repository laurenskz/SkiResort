            package nl.baboea.android.skiresort.game;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import nl.baboea.android.skiresort.Camera;
import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 4-12-2015.
 */
public class EnvironmentSpawner implements ParticipantContainer{

    public static final float GENERATION_WIDTH = 4f, GENERATION_HEIGHT = 6f;
    public static final String TAG = "Environmentspawner";
    private ArrayList<Vec3> pointsAtGenerated = new ArrayList<>();
    private Vec3 lastTile = null;
    private GenerationTileFiller filler = new GenerationTileFiller(null,GENERATION_WIDTH,GENERATION_HEIGHT);

    public EnvironmentSpawner(){

    }

    private Vec3 worldPointToGrid(Vec3 worldPoint){
        float x = (float)(Math.floor(worldPoint.getX() / GENERATION_WIDTH))*GENERATION_WIDTH;//X has to be floored
        float y = (float)((int)(worldPoint.getY()/GENERATION_HEIGHT))*GENERATION_HEIGHT;//Y has to be ceiled when negative and floored when positive
        Vec3 gridPoint = new Vec3(x,y,0);
        //Log.d(TAG, "worldPointToGrid worldPoint:"+worldPoint + " gridPoint:"+gridPoint );
        return gridPoint;
    }

    private ArrayList<GameParticipant> generateAt(Vec3 gridPoint){
        if(alreadyGenerated(gridPoint))return null;
        //Log.d(TAG, "generateAt gridPoint = " + gridPoint);
        filler.setPoint(gridPoint);
        lastTile = gridPoint;//Save it for a little optimization.
        filler.generate();//This makes the filler work
        pointsAtGenerated.addAll(filler.getAllGeneratedGridPoints());
        //Log.d(TAG, "generateAt pointsGenerated size = " + pointsAtGenerated.size());
        cleanUpPoints();//Here we do some cleaning up, shouldn't have to take place too often
        return filler.getAll();
    }

    private ArrayList<GameParticipant> generateAtCameraPosition(Vec3 cameraPosition) {
        ArrayList<Vec3> gridTiles = getAllGridPositions(cameraPosition);
        ArrayList<GameParticipant> toReturn = null;
        for(Vec3 gridTile : gridTiles){
            if(toReturn==null){
                toReturn=generateAt(gridTile);
            }else{
                ArrayList<GameParticipant> newParticipants = generateAt(gridTile);
                if(newParticipants!=null)toReturn.addAll(newParticipants);
            }
        }
        if(toReturn!=null){
            //Log.d(TAG, "generateAtCameraPosition cam pos = " + cameraPosition);
        }
        return toReturn;
    }

    private ArrayList<Vec3> getAllGridPositions(Vec3 cameraPosition) {
        ArrayList<Vec3> gridTiles = new ArrayList<>();
        float middleToTop = (1f/ Constants.RATIO)+0.7f;
        float screenBottom = cameraPosition.getY()- middleToTop;
        float screenTop = cameraPosition.getY()+ middleToTop;
        Vec3 screenLeft = new Vec3(cameraPosition.getX()-1.1f,screenBottom,0);
        Vec3 screenRight = new Vec3(cameraPosition.getX()+1.1f,screenBottom,0);
        Vec3 screenTopLeft = new Vec3(cameraPosition.getX()-1.1f,screenTop,0);
        Vec3 screenTopRight = new Vec3(cameraPosition.getX()+1.1f,screenTop,0);
        addWorldPointToArray(screenLeft,gridTiles);
        addWorldPointToArray(screenRight,gridTiles);
        addWorldPointToArray(screenTopLeft,gridTiles);
        addWorldPointToArray(screenTopRight,gridTiles);
        return gridTiles;
    }

    private void addWorldPointToArray(Vec3 worldPoint, ArrayList<Vec3> array){
        Vec3 grid = worldPointToGrid(worldPoint);
        if(!array.contains(grid))array.add(grid);
    }

    private boolean alreadyGenerated(Vec3 gridPoint){
        if(lastTile==null)return false;//IF it is null it has not been initialized yet.
        if(lastTile.equals(gridPoint,0.1f))return true;//Player is in the last tile majority of time.
        for(Vec3 generated : pointsAtGenerated){
            if(gridPoint.equals(generated,0.1f))return true;//Second parameter is epsilon. We don't care really much for precision here
        }
        return false;
    }


    public ArrayList<Vec3> getPointsAtGenerated() {
        return pointsAtGenerated;
    }

    private void cleanUpPoints(){
        Iterator<Vec3> iterator = pointsAtGenerated.iterator();
        while(iterator.hasNext()){
            Vec3 point = iterator.next();
            if(point.getY()-GENERATION_HEIGHT> Camera.getPosition().getY()+(1f/ Constants.RATIO))iterator.remove();
        }
    }

    @Override
    public ArrayList<GameParticipant> getAll() {
        pointsAtGenerated.add(worldPointToGrid(Camera.getPosition()));//Player can have 1 free point
        return new ArrayList<GameParticipant>();//Just return an empty list and add the positions to the array.
    }

    @Override
    public GameParticipant[] update(float secondsElapsed) {
        ArrayList<GameParticipant> toReturn = generateAtCameraPosition(Camera.getPosition());
        if(toReturn==null)return null;
        return toReturn.toArray(new GameParticipant[toReturn.size()]);
    }
}
