package dev.mv.engine.render.draw;

import lombok.Getter;

public class sSolidColor {

    @Getter
    float[] color;

    public sSolidColor(int r, int g, int b, int a){
        this.color = new float[]{
                (float)(r/255.0f), (float)(g/255.0f), (float)(b/255.0f), (float)(a/255.0f)
        };
    }
}