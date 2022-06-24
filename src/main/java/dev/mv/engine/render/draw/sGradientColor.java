package dev.mv.engine.render.draw;

import lombok.Getter;

public class sGradientColor {

    @Getter
    float[] color;

    public sGradientColor(int r0, int g0, int b0, int a0,
                          int r1, int g1, int b1, int a1,
                          int r2, int g2, int b2, int a2,
                          int r3, int g3, int b3, int a3) {
        this.color = new float[] {
                (float)(r0/255.0f), (float)(g0/255.0f), (float)(b0/255.0f), (float)(a0/255.0f),
                (float)(r1/255.0f), (float)(g1/255.0f), (float)(b1/255.0f), (float)(a1/255.0f),
                (float)(r2/255.0f), (float)(g2/255.0f), (float)(b2/255.0f), (float)(a2/255.0f),
                (float)(r3/255.0f), (float)(g3/255.0f), (float)(b3/255.0f), (float)(a3/255.0f)
        };
    }

    public sGradientColor(float[] color) {
        this.color = color;
    }

}
