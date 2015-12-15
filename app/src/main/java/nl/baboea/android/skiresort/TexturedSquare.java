package nl.baboea.android.skiresort;

/**
 * Created by Laurens on 14-9-2015.
 */
public class TexturedSquare extends AbstractShape {


    private static final float[] textureCoordinates = new float[]{
            0,0,
            0,1,
            1,1,
            1,0
    };

    private static final short[] indices = new short[]{
            0, 1, 2,
            0, 2, 3
    };



    public TexturedSquare(Texture texture) {
        super(texture.getVertices(), textureCoordinates ,indices, ShaderProgram.basicShader,texture);
    }


}
