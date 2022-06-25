package dev.mv.engine.render.draw;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.render.Display;
import dev.mv.engine.render.textures.Texture;

public class Draw {

    private static Display d;
    private static RenderBatch batch;

    public static void setColor(int r, int g, int b, int a) {
        batch.getShader().setUniform1f("r", (float)r/255.0f);
        batch.getShader().setUniform1f("g", (float)g/255.0f);
        batch.getShader().setUniform1f("b", (float)b/255.0f);
        batch.getShader().setUniform1f("a", (float)a/255.0f);
    }

    public static void fillRect(int x1, int y1, int width1, int height1, int zLayer, sSolidColor color) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;
        float z = (float) zLayer / 100.0f;

        float[] vertices = new float[]{
                x, y, z,					    color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],     0.0f, 0.0f,     0.0f,
                x + width, y, z,			    color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],     0.0f, 0.0f,     0.0f,
                x, y - height, z,		        color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],     0.0f, 0.0f,     0.0f,
                x + width, y - height, z,       color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],     0.0f, 0.0f,      0.0f
        };

       batch.addVertexFloatArrayToBatch(vertices);

    }

    public static void fillRect(int x1, int y1, int width1, int height1, int zLayer, sGradientColor color) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;
        float z = (float) zLayer / 100.0f;

        float[] vertices = new float[]{
                x, y, z,					    color.getColor()[0], color.getColor()[1], color.getColor()[2], color.getColor()[3],        0.0f, 0.0f,     0.0f,
                x + width, y, z,			    color.getColor()[4], color.getColor()[5], color.getColor()[6], color.getColor()[7],        0.0f, 0.0f,     0.0f,
                x, y - height, z,		        color.getColor()[8], color.getColor()[9], color.getColor()[10], color.getColor()[11],      0.0f, 0.0f,     0.0f,
                x + width, y - height, z,       color.getColor()[12], color.getColor()[13], color.getColor()[14], color.getColor()[15],    0.0f, 0.0f,     0.0f
        };

        batch.addVertexFloatArrayToBatch(vertices);
    }

    public static void drawColorWheel(int x, int y, int width, int height) {
        int xoff = width / 2;
        int yoff = height / 2;
        Draw.fillRect(x, y, xoff, yoff, 0, new sGradientColor(255, 0, 0, 255, 255, 0, 127, 255, 255, 255, 0, 255, 255, 255, 255, 255));
        Draw.fillRect(x + xoff, y, xoff, yoff, 0, new sGradientColor(255, 0, 127, 255, 255, 0, 255, 255, 255, 255, 255, 255, 0, 0, 255, 255));
        Draw.fillRect(x, y + yoff, xoff, yoff, 0, new sGradientColor(255, 255, 0, 255, 255, 255, 255, 255, 0, 255, 0, 255, 0, 255, 127, 255));
        Draw.fillRect(x + xoff, y + yoff, xoff, yoff, 0, new sGradientColor(255, 255, 255, 255, 0, 0, 255, 255, 0, 255, 127, 255, 0, 255, 255, 255));
    }

    public static void drawImage(int x1, int y1, int width1, int height1, int zLayer, Texture tex) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;
        float z = (float) zLayer / 100.0f;

        batch.addTexture(tex);

        float[] vertices = new float[] {
                x, y, z,					    0.0f, 0.0f, 0.0f, 0.0f,     1.0f, 0.0f,     tex.getID(),
                x + width, y, z,			    0.0f, 0.0f, 0.0f, 0.0f,     0.0f, 0.0f,     tex.getID(),
                x, y - height, z,		        0.0f, 0.0f, 0.0f, 0.0f,     1.0f, 1.0f,     tex.getID(),
                x + width, y - height, z,       0.0f, 0.0f, 0.0f, 0.0f,     0.0f, 1.0f,     tex.getID()
        };

        batch.addVertexFloatArrayToBatch(vertices);
    }

    public static void init(Display display, RenderBatch renderBatch) {
        d = display;
        batch = renderBatch;
    }
    public static void init(Display display) throws ShaderCreateException, ShaderLinkException {
        d = display;
        batch = new RenderBatch(1000);
    }

    public static RenderBatch getBatch(){
        return batch;
    }
}


