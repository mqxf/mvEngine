package dev.mv.engine.math.vector;

import lombok.Getter;
import lombok.Setter;

public class Vector3f {

    @Getter @Setter
    private float x, y, z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float[] flatten() {
        return new float[] {x, y, z};
    }

}
