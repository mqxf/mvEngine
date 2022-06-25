package dev.mv.engine.render.handler;

import dev.mv.engine.render.Display;

import java.io.IOException;

public interface DisplayManager {

    public void start(Display d) throws IOException;
    public void update();

}
