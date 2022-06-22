package dev.mv.engine.render.draw;

import dev.mv.engine.render.Display;
import dev.mv.engine.render.textures.Texture;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Draw {

    static FloatBuffer vecBuffer;
    static IntBuffer indBuffer;

    //private Renderer renderer = new Renderer();

    private static int drawCount = 0;
    private static int v_id;
    private static int i_id;
    private static int t_id;
    private static Display d;

    public static void setColor(int r, int g, int b, int a) {
        d.setUniform1f("r", (float)r/255.0f);
        d.setUniform1f("g", (float)g/255.0f);
        d.setUniform1f("b", (float)b/255.0f);
        d.setUniform1f("a", (float)a/255.0f);
    }

    public static void fillRect(int x1, int y1, int width1, int height1) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;

        int[] indices = {0, 2, 3, 0, 1, 3};
        float[] vertices = {
                x, y, 0.0f,					 0.0f, 0.0f,
                x + width, y, 0.0f,			 0.0f, 0.0f,
                x, y - height, 0.0f,		 0.0f, 0.0f,
                x + width, y - height, 0.0f, 0.0f, 0.0f,
        };
        //render(vertices, indices, false, GL_TRIANGLES);
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

    public static void drawImage(int x1, int y1, int width1, int height1, Texture tex) {
        float x = (float)x1/(float)d.getWidth()*2.0f-1.0f;
        float y = ((float)y1/(float)d.getHeight()*2.0f-1.0f)*-1.0f;
        float width = (float)width1/(float)d.getWidth()*2.0f;
        float height = (float)height1/(float)d.getHeight()*2.0f;

        int[] indices = {0, 1, 2, 3, 1, 2};
        float[] vertices = {
                x, y, 0.0f,                  1.0f, 0.0f,
                x + width, y, 0.0f,          0.0f, 0.0f,
                x, y - height, 0.0f,         1.0f, 1.0f,
                x + width, y - height, 0.0f, 0.0f, 1.0f,
        };
//		float[] texCoords = {
//			0.0f, 0.0f,
//			1.0f, 0.0f,
//			1.0f, 1.0f,
//			1.0f, 1.0f,
//			0.0f, 1.0f,
//			0.0f, 0.0f,
//		};

        d.setUniform1i("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        tex.bind();

        vecBuffer.put(vertices);
        vecBuffer.flip();
        indBuffer.put(indices);
        indBuffer.flip();

        render(indices.length, true, GL_TRIANGLES);
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

    public static void init(Display display) {
        d = display;
        glfwMakeContextCurrent(d.winAddr);
        vecBuffer = BufferUtils.createFloatBuffer(4*5);
        indBuffer = BufferUtils.createIntBuffer(6);

        v_id = glGenBuffers();
        t_id = glGenBuffers();
        i_id = glGenBuffers();
    }
}


