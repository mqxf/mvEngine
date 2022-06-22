package dev.mv.engine;

import dev.mv.engine.render.Display;
import dev.mv.engine.render.draw.RenderBatch;
import dev.mv.engine.render.handler.DisplayManager;

public class Main implements DisplayManager {

    RenderBatch batch;

    public static void main(String[] args) {
        Display win = new Display("text", 500, 500, false, new Main());
        win.run();
    }

    @Override
    public void start() {
        batch = new RenderBatch(1000);
        batch.addVertexArrayToBatch(new float[] {0.2f, 0.2f, 0.0f,  //1.0f, 0.0f, 0.0f,
                                                -0.2f, 0.2f, 0.0f,  //0.0f, 1.0f, 0.0f,
                                                0.2f, -0.2f, 0.0f,  //0.0f, 0.0f, 1.0f,
                                                -0.2f, -0.2f, 0.0f, });// 1.0f, 0.0f, 1.0f,
// 1.0f, 0.0f, 0.0f,
        batch.addVertexArrayToBatch(new float[] {0.2f, -0.3f, 0.0f, // 0.0f, 1.0f, 0.0f,
                                                -0.2f, -0.3f, 0.0f, //0.0f, 0.0f, 1.0f,
                                                0.2f, -0.7f, 0.0f,  // 1.0f, 0.0f, 1.0f,
                                                -0.2f, -0.7f, 0.0f, });//
        batch.pushBatchToGPU();
    }

    @Override
    public void update() {
        batch.render();
    }
}