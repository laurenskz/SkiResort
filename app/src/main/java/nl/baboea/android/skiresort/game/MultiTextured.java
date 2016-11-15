package nl.baboea.android.skiresort.game;

import java.util.HashMap;
import java.util.Objects;

import nl.baboea.android.skiresort.AbstractShape;
import nl.baboea.android.skiresort.Texture;
import nl.baboea.android.skiresort.TexturedSquare;

/**
 * Created by Laurens on 15-11-2015.
 */
public abstract class MultiTextured extends GameParticipant{

    protected Enum selected;
    private static HashMap<Object,TexturedSquare[]> textures = new HashMap<>();
    private Object key = this.getClass();

    public MultiTextured(int[] drawableIDS) {
        initialize(drawableIDS);
    }

    public MultiTextured(int[] drawableIDS,String id) {
        key = id;
        initialize(drawableIDS);
    }

    public MultiTextured(int[] drawableIDS,boolean usesCollisionMap,String key){
        this.key = key;
        initializeWithCollisionMap(drawableIDS);
    }

    public MultiTextured(int[] drawableIDS,boolean usesCollisionMap){
        initializeWithCollisionMap(drawableIDS);
    }

    private void initializeWithCollisionMap(int[] drawableIDS){
        selected = null;
        if(textures.get(key)==null){
            TexturedSquare[] textures = new TexturedSquare[drawableIDS.length/2];
            for(int i = 0 ; i < drawableIDS.length ; i+=2){
                textures[i] = new TexturedSquare(new Texture(drawableIDS[i],drawableIDS[i+1]));
            }
            MultiTextured.textures.put(key,textures);
        }
    }

    private void initialize(int[] drawableIDS) {
        selected = null;
        if(textures.get(key)==null){
            TexturedSquare[] textures = new TexturedSquare[drawableIDS.length];
            int i = 0;
            for(int id : drawableIDS){
                textures[i] = new TexturedSquare(new Texture(id));
                i++;
            }
            MultiTextured.textures.put(key,textures);
        }
    }


    public AbstractShape getShape(){
        if(selected==null){
            return textures.get(key)[0];
        }
        return textures.get(key)[selected.ordinal()];
    }
}
