package dev.mv.engine.render.draw;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.render.Display;
import dev.mv.engine.render.textures.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Draw {

    static FloatBuffer vecBuffer;
    static IntBuffer indBuffer;

    private static int drawCount = 0;
    private static int v_id;
    private static int i_id;
    private static int t_id;
    private static int usedTextures = 1;
    private int[] texSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
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

    public static void stripRect(int x1, int y1, int width1, int height1, int depth) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;

        glLineWidth((float)depth);

        int[] indices = {0, 1, 1, 3, 2, 0, 2, 3};
        float[] vertices = {
                x, y, 0.0f,					 0.0f, 0.0f,
                x + width, y, 0.0f,			 0.0f, 0.0f,
                x, y - height, 0.0f,		 0.0f, 0.0f,
                x + width, y - height, 0.0f, 0.0f, 0.0f,
        };
        //render(vertices, indices, false, GL_LINES);
    }

    public static void drawImage(int x1, int y1, int width1, int height1, int zLayer, Texture tex) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;
        float z = (float) zLayer / 100.0f;

        float[] vertices = new float[] {
                x, y, z,					    0.0f, 0.0f, 0.0f, 0.0f,     1.0f, 0.0f,     0.0f,
                x + width, y, z,			    0.0f, 0.0f, 0.0f, 0.0f,     0.0f, 0.0f,     0.0f,
                x, y - height, z,		        0.0f, 0.0f, 0.0f, 0.0f,     1.0f, 1.0f,     0.0f,
                x + width, y - height, z,       0.0f, 0.0f, 0.0f, 0.0f,     0.0f, 1.0f,     0.0f
        };

        batch.addVertexFloatArrayToBatch(vertices);
    }

    private static void render(int drawCount, boolean tex, int mode) {

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, vecBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);

        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*Float.BYTES, 3*Float.BYTES);
        glEnableVertexAttribArray(1);

        glDrawElements(mode, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void init(Display display, RenderBatch renderBatch) {
        d = d;
        batch = batch;
    }
    public static void init(Display display) throws ShaderCreateException, ShaderLinkException {
        d = display;
        batch = new RenderBatch(1000);
    }

    public static RenderBatch getBatch(){
        return batch;
    }
}


