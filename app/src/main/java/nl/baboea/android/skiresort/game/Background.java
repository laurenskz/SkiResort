package nl.baboea.android.skiresort.game;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import nl.baboea.android.skiresort.Camera;
import nl.baboea.android.skiresort.Constants;
import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 19-11-2015.
 */
public class Background implements ParticipantContainer {

    public static final String TAG = "Background";
    private SnowTile[][] tiles;
    private SnowTile[] highestRow;
    private float tileWidth, tileHeight;
    private float needed;
    private float xLeft, xRight;

    public Background(){
        needed = (float)1/Constants.RATIO;
        //First make 1 snow tile to get its size (has to be done anyway, this creates the texture and vao)
        SnowTile tile = new SnowTile();
        tileWidth =  tile.getShape().getWidth();
        tileHeight = tile.getShape().getHeight();
        Log.d(TAG, "Width = " + Constants.WIDTH + " Height = " + Constants.HEIGHT);
        int neededTilesY = (int)((float)2/(tileHeight*((float)Constants.WIDTH/Constants.HEIGHT)));
        neededTilesY+=2;//We need to roof the actual number and add one so we can ski(move tiles around)
        int neededTilesX = (int)((float)2/tileWidth);
        neededTilesX+=2;
        Log.d(TAG, "Background tiles x = " + neededTilesX + " tiles y = " + neededTilesY);
        tiles = new SnowTile[neededTilesY][neededTilesX];
        float toMiddleX = tileWidth/2;
        float toMiddleY = tileHeight/2;
        tiles[0][0] = tile;//Store the first tile
        xLeft = -1+toMiddleX - (tileWidth/2);
        tiles[0][0].getModel().getPosition().setX(-1+toMiddleX);
        tiles[0][0].getModel().getPosition().setY(-1 +toMiddleY);
        for(int y = 0 ; y < tiles.length ; y++){
            for(int x = 0 ; x < tiles[y].length ; x++){
                if(x==0&&y==0)continue;
                tiles[y][x] = new SnowTile();
                float xPos = -1+x*tileHeight+toMiddleX;
                float yPos = -1+y*tileWidth+toMiddleY;
                tiles[y][x].getModel().getPosition().setX(xPos);
                tiles[y][x].getModel().getPosition().setY(yPos);
            }
        }
        xRight = -1+(tiles[0].length-1)*tileHeight+toMiddleX+(tileWidth/2);
        highestRow = tiles[tiles.length-1];

    }

    @Override
    public ArrayList<GameParticipant> getAll() {
        ArrayList<GameParticipant> x = new ArrayList<>();
        for(int i = 0 ; i<tiles.length; i++)
            Collections.addAll(x,tiles[i]);
        return x;

    }

    @Override
    public GameParticipant[] update(float secondsElapsed) {
        Vec3 cameraPosition = Camera.getPosition();
        checkHeight(cameraPosition);
        checkWidth(cameraPosition);
        return null;
    }

    private void checkWidth(Vec3 cameraPosition){
        float spaceToLeft = cameraPosition.getX() - xLeft;
        float spaceToRight = xRight - cameraPosition.getX();
        if(spaceToLeft>=1&&spaceToRight>=1)return;//Everything is fine, the map does not exceed the boundaries.
        if(spaceToLeft<1){
            moveRowsX(false,xRight-tileWidth+(tileWidth/10),-(tiles.length-1)*tileWidth);//Left is false because we want to move the right rows
            xLeft-=tileWidth;
            xRight-=tileWidth;
            return;//We moved the rows and now we're done!
        }
        moveRowsX(true,xLeft+tileWidth-(tileWidth/10),(tiles.length-1)*tileWidth);
        xLeft+=tileWidth;
        xRight+=tileWidth;
    }

    private void moveRowsX(boolean left, float toPass, float toIncrement){
        for(SnowTile[] tileRow : tiles){
            for(SnowTile tile : tileRow){
                if(left){
                    if(tile.getModel().getPosition().getX()<toPass)
                        tile.getModel().getPosition().incrementX(toIncrement);
                }else{
                    if(tile.getModel().getPosition().getX()>toPass)
                        tile.getModel().getPosition().incrementX(toIncrement);
                }
            }
        }
    }

    private void checkHeight(Vec3 cameraPosition) {
        float highestY = highestRow[0].getModel().getPosition().getY()+(tileHeight/2);
        highestY-=tileHeight;//We want to be one row down
        if((highestY-cameraPosition.getY())>needed) {
            for(SnowTile tile : highestRow){
                tile.getModel().getPosition().incrementY(-1*tiles.length*tileHeight);
            }
            float highest = -Float.MAX_VALUE;
            for(SnowTile[] row : tiles){
                float y = row[0].getModel().getPosition().getY();
                if(y>highest){
                    highestRow = row;
                    highest = y;
                }
            }
        }
    }
}

