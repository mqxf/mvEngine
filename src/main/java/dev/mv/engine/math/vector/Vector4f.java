package dev.mv.engine.math.vector;

import lombok.Getter;
import lombok.Setter;

public class Vector4f {

    @Getter @Setter
    private float x, y, z, w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }



}
