package nl.baboea.android.skiresort;

import nl.baboea.android.skiresort.nl.baboea.android.skiresort.math.Vec3;

/**
 * Created by Laurens on 15-11-2015.
 */
public class Model {
    private Vec3 rotation = new Vec3();
    private Vec3 position = new Vec3();
    private Vec3 scale = new Vec3(1f,1f,1f);


    /**
     *
     * @return the rotation vector of this object
     */
    public Vec3 getRotation() {
        return rotation;
    }

    /**
     *
     * @return The world position of this object
     */
    public Vec3 getPosition() {
        return position;
    }

    /**
     *
     * @return The scale of this object
     */
    public Vec3 getScale() {
        return scale;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setScale(Vec3 scale) {
        this.scale = scale;
    }
}
