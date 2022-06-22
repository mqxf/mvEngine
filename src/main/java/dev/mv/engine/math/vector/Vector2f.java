package dev.mv.engine.math.vector;

import lombok.Getter;
import lombok.Setter;

public class Vector2f {

    @Getter @Setter
    private float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
