package nl.baboea.android.skiresort.game;

import java.util.HashMap;

import nl.baboea.android.skiresort.AbstractShape;
import nl.baboea.android.skiresort.Texture;
import nl.baboea.android.skiresort.TexturedSquare;

/**
 * Created by Laurens on 15-11-2015.
 */
public abstract class MultiTextured extends GameParticipant{

    protected Enum selected;
    private static HashMap<Class,TexturedSquare[]> textures = new HashMap<>();

    public MultiTextured(int[] drawableIDS) {
        initialize(drawableIDS);
    }

    public MultiTextured(int[] drawableIDS,boolean usesCollisionMap){
        initializeWithCollisionMap(drawableIDS);
    }

    private void initializeWithCollisionMap(int[] drawableIDS){
        selected = null;
        if(textures.get(this.getClass())==null){
            TexturedSquare[] textures = new TexturedSquare[drawableIDS.length/2];
            for(int i = 0 ; i < drawableIDS.length ; i+=2){
                textures[i] = new TexturedSquare(new Texture(drawableIDS[i],drawableIDS[i+1]));
            }
            MultiTextured.textures.put(this.getClass(),textures);
        }
    }

    private void initialize(int[] drawableIDS) {
        selected = null;
        if(textures.get(this.getClass())==null){
            TexturedSquare[] textures = new TexturedSquare[drawableIDS.length];
            int i = 0;
            for(int id : drawableIDS){
                textures[i] = new TexturedSquare(new Texture(id));
                i++;
            }
            MultiTextured.textures.put(this.getClass(),textures);
        }
    }


    public AbstractShape getShape(){
        if(selected==null){
            return textures.get(this.getClass())[0];
        }
        return textures.get(this.getClass())[selected.ordinal()];
    }
}
