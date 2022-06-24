package dev.mv.engine.render.draw;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.math.vector.Vector3f;
import dev.mv.engine.render.textures.Texture;
import dev.mv.engine.shader.Shader;
import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class RenderBatch {

    private int used;
    private int maxSize;
    private RenderBatch child;
    private float[] data;
    private int vbuffer_id;
    private int ibuffer_id;
    private int bufferIndex = 0;
    private static ArrayList<Texture> textures;

    @Getter
    private Shader shader;

    private FloatBuffer vbo;
    private IntBuffer ibo;

    private int[] indices;

    private ByteBuffer buffer;

    //pos    color    texCoords   texID
    //f f f  f f f f  f f         f

    private static final int POS_SIZE = 3;
    private static final int COLOR_SIZE = 4;
    private static final int TEX_SIZE = 2;
    private static final int TEX_ID_SIZE = 1;

    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_SIZE + TEX_ID_SIZE;

    private static final int VERTEX_BUFFER_INDEX = VERTEX_SIZE * 4;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;
    private static int BUFFER_SIZE = 0;



    public RenderBatch(int maxSize) throws ShaderCreateException, ShaderLinkException {
        this.child = null;
        this.used = 0;
        this.maxSize = maxSize;
        textures = new ArrayList<>();
        setupBatch();
    }

    private void setupBatch() throws ShaderLinkException, ShaderCreateException {
        this.BUFFER_SIZE = this.maxSize * VERTEX_SIZE_BYTES;
        this.data = new float[this.BUFFER_SIZE];
        this.indices = new int[this.maxSize * 6];
        this.vbuffer_id = glGenBuffers();
        this.ibuffer_id = glGenBuffers();
        buffer.allocate(this.maxSize * VERTEX_SIZE_BYTES);
        glBufferData(GL_ARRAY_BUFFER, this.BUFFER_SIZE, GL_DYNAMIC_DRAW);

        this.vbo = BufferUtils.createFloatBuffer(this.BUFFER_SIZE);
        this.ibo = BufferUtils.createIntBuffer(this.maxSize * 6);

        shader = new Shader("/shaders/default.vs", "/shaders/default.fs");

        shader.make();
        shader.use();
    }

    private void generateQuadIndicies() {
        this.indices[this.bufferIndex * 6 + 0] = 0 + this.bufferIndex * 4;
        this.indices[this.bufferIndex * 6 + 1] = 2 + this.bufferIndex * 4;
        this.indices[this.bufferIndex * 6 + 2] = 3 + this.bufferIndex * 4;
        this.indices[this.bufferIndex * 6 + 3] = 0 + this.bufferIndex * 4;
        this.indices[this.bufferIndex * 6 + 4] = 1 + this.bufferIndex * 4;
        this.indices[this.bufferIndex * 6 + 5] = 3 + this.bufferIndex * 4;
    }

    public void addVertexFloatArrayToBatch(float[] data) {
        if (used >= maxSize - 1) {
            pushToChild(data);
            return;
        }
        generateQuadIndicies();
        for(int i = 0; i < data.length; i++) {
            this.data[this.bufferIndex * VERTEX_BUFFER_INDEX + i] = data[i];
        }
        this.bufferIndex++;
        this.used++;
    }

    public void addVertexVectorArrayToBatch(Vector3f[] data) {
        float[] ffdata = new float[data.length * 3];
        for (int i = 0; i < data.length; i++) {
            float[] ffdatai = data[i].flatten();
            for (int j = 0; j < 3; j++) {
                ffdata[i * 3 + j] = ffdatai[j];
            }
        }
        addVertexFloatArrayToBatch(ffdata);
    }

    public void pushBatchToGPU() {

        //System.out.println(Arrays.toString(this.data));
        this.vbo.put(this.data);
        this.ibo.put(this.indices);

        this.vbo.flip();
        this.ibo.flip();
    }

    public void render() {

        glBindBuffer(GL_ARRAY_BUFFER, this.vbuffer_id);
        glBufferData(GL_ARRAY_BUFFER, this.vbo, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ibuffer_id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.ibo, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, VERTEX_SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_SIZE * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, VERTEX_SIZE_BYTES, (POS_SIZE + COLOR_SIZE) * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, 1, GL_FLOAT, false, VERTEX_SIZE_BYTES, (POS_SIZE + COLOR_SIZE + TEX_SIZE) * Float.BYTES);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, indices.length * (this.bufferIndex + 1), GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        reset();
    }

    private void reset() {
        this.bufferIndex = 0;
        this.used = 9;
    }

    private void pushToChild(float[] data) {
        createChild();
        child.addVertexFloatArrayToBatch(data);
    }

    private void createChild() {
        if (child == null) {
            try {
                child = new RenderBatch(maxSize);
            } catch (ShaderCreateException | ShaderLinkException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public RenderBatch getChild() {
        return this.child;
    }

    public ArrayList<RenderBatch> getRecursiveChildrenAsArray() {
        ArrayList<RenderBatch> children = new ArrayList<>();
        RenderBatch child = this.child;
        while (child != null) {
            children.add(child);
            child = child.getChild();
        }
        return children;
    }

}