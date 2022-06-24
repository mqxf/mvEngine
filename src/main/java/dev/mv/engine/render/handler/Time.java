package dev.mv.engine.render.handler;

import lombok.Getter;
import lombok.Setter;

public class Time {

    @Getter @Setter
    private static double delta = 0.0f;
    @Getter @Setter
    private static int fps = 0;

}
