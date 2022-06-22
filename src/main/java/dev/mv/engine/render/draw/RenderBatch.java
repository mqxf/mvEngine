package dev.mv.engine.render.draw;

import dev.mv.engine.shader.Shader;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class RenderBatch {

    private int maxSize;
    private float[] data;
    private int vbuffer_id;
    private int ibuffer_id;
    private int bufferIndex = 0;

    private Shader shader = new Shader("./shaders/default.vs", "./shaders/default.fs");

    private FloatBuffer vbo;
    private IntBuffer ibo;

    private int[] indices = {0, 1, 2, 3, 1, 2,
                            4, 5, 6, 7, 5, 6};

    private ByteBuffer buffer;

    //pos    color
    //f f f  f f f

    private static final int POS_SIZE = 12;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_COORD_SIZE = 4;
    private static final int TEX_SLOT_ID_SIZE = 1;

    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORD_SIZE + TEX_SLOT_ID_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private static int BUFFER_SIZE = 0;



    public RenderBatch(int maxSize) {
        this.maxSize = maxSize;
        setupBatch();
    }

    private void setupBatch() {
        this.BUFFER_SIZE = this.maxSize * VERTEX_SIZE_BYTES;
        this.data = new float[this.BUFFER_SIZE];
        this.vbuffer_id = glGenBuffers();
        this.ibuffer_id = glGenBuffers();
        buffer.allocate(this.maxSize * VERTEX_SIZE_BYTES);
        glBufferData(GL_ARRAY_BUFFER, this.BUFFER_SIZE, GL_DYNAMIC_DRAW);

        this.vbo = BufferUtils.createFloatBuffer(this.BUFFER_SIZE);
        this.ibo = BufferUtils.createIntBuffer(12);

        shader.make();
        shader.use();
    }

    public void addVertexArrayToBatch(float[] data) {
        for(int i = 0; i < data.length; i++) {
            this.data[this.bufferIndex * 12 + i] = data[i];
        }
        this.bufferIndex++;
    }

    public void pushBatchToGPU() {

        System.out.println(Arrays.toString(this.data));
        this.vbo.put(this.data);
        this.ibo.put(this.indices);

        this.vbo.flip();
        this.ibo.flip();
    }

    public void render() {

        glBindBuffer(GL_ARRAY_BUFFER, vbuffer_id);
        glBufferData(vbuffer_id, this.data, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, this.vbuffer_id);
        glBufferData(GL_ARRAY_BUFFER, this.vbo, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibuffer_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.ibo, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3*Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        //glVertexAttribPointer(1, 3, GL_FLOAT, false, 6*Float.BYTES, 3*Float.BYTES);
        //glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, indices.length * (this.bufferIndex + 1), GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
