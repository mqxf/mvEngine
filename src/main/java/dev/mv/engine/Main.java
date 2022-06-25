package dev.mv.engine;

import dev.mv.engine.TerrainTest.DrawTiles;
import dev.mv.engine.TerrainTest.Terrain;
import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.render.Display;
import dev.mv.engine.render.draw.Draw;
import dev.mv.engine.render.draw.RenderBatch;
import dev.mv.engine.render.handler.DisplayManager;
import dev.mv.engine.render.textures.Texture;

import java.io.IOException;
import java.util.Random;

public class Main implements DisplayManager {

    RenderBatch batch;
    Random random;
    Texture md;
    Texture lol;
    Texture guy;

    int[][] terrain;

    public static void main(String[] args) throws IOException, ShaderCreateException, ShaderLinkException {
        Display win = new Display("tester window", 1000, 1000, false, new Main());
        win.run();
    }

    @Override
    public void start(Display d) {
        random = new Random();
        batch = Draw.getBatch();
        md = new Texture("/textures/medDemanjo.png");
        lol = new Texture("/textures/cultextur.png");
        guy = new Texture("/textures/littleGuy.png");

        terrain = Terrain.generateTerrain(d);
        /*
        batch.addVertexFloatArrayToBatch(new float[] {0.2f, 0.5f, 0.0f,     1.0f, 0.0f, 0.0f,//  tr
                                                -0.2f, 0.5f, 0.0f,     0.0f, 1.0f, 0.0f,//  tl
                                                0.2f, 0.3f, 0.0f,     0.0f, 0.0f, 1.0f,//  br
                                                -0.2f, 0.3f, 0.0f,    1.0f, 0.0f, 1.0f,});//  bl

        batch.addVertexFloatArrayToBatch(new float[] {0.2f, -0.3f, 0.0f,     1.0f, 0.0f, 0.0f,
                                                -0.2f, -0.3f, 0.0f,    0.0f, 1.0f, 0.0f,
                                                0.2f, -0.7f, 0.0f,     0.0f, 00f, 1.0f,
                                                -0.2f, -0.7f, 0.0f,    1.0f, 00f, 1.0f});

        */
    }

    @Override
    public void update() {
        DrawTiles.draw(terrain);
    }
}