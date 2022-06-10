package dev.mv.engine.render.draw;

import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.math.vector.Vector2f;
import dev.mv.engine.math.vector.Vector4f;
import dev.mv.engine.render.textures.Texture;
import dev.mv.engine.shader.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // pos, pos, 	col, col, col, col,		texCoord, texCoord, 	texID

    private final int POS_SIZE = 2;
    private final int COL_SIZE = 4;
    private final int TEX_COORD_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COl_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORD_OFFSET = COl_OFFSET + COL_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORD_OFFSET + TEX_COORD_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private int spriteNum;
    private boolean hasRoom;

    private Sprite[] sprites;
    private List<Texture> textures;

    private Shader shader;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private int vaoID;
    private int vboID;
    private int maxSize;

    public RenderBatch(int maxSize) {
        shader = new Shader("./shaders/shader.vs", "./shaders/shader.fs");

        vertices = new float[maxSize * 4 * VERTEX_SIZE];

        this.spriteNum = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<Texture>();
        this.sprites = new Sprite[maxSize];

        this.maxSize = maxSize;
    }

    public void init() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        Vector2f v = new Vector2f(1, 1);
        v.getX();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COL_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COl_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORD_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORD_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void addSprite(Sprite sprite) {
        int index = this.spriteNum;
        this.sprites[index] = sprite;
        this.spriteNum++;

        if(sprite.getTexture() != null) {
            if(!textures.contains(sprite.getTexture())) {
                textures.add(sprite.getTexture());
            }
        }

        loadVertexProperties(index);

        if(this.spriteNum >= this.maxSize) {
            this.hasRoom = false;
        }
    }

    public void render() throws ShaderLinkException {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();

        for(int i=0;i<textures.size();i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.spriteNum * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(int i=0;i<textures.size();i++) {
            textures.get(i).unbind();
        }
    }

    private void loadVertexProperties(int index) {
        Sprite sprite = this.sprites[index];

        int offset = index * 4 * VERTEX_SIZE;

        int texID = 0;

        if(sprite.getTexture() != null) {
            for(int i=0;i<textures.size();i++) {
                if(textures.get(i) == sprite.getTexture()) {
                    texID = i + 1;
                    break;
                }
            }
        }

        float xAdd = 1.0f;
        float yAdd = 1.0f;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        for(int i=0;i<4;i++) {
            if(i == 1)yAdd = 0.0f;
            else if(i == 1)xAdd = 0.0f;
            else if(i == 1)yAdd = 1.0f;

            //load pos
            vertices[offset] = sprite.getPosition().x + (xAdd * sprite.getScale().x);
            vertices[offset + 1] = sprite.getPosition().y + (yAdd * sprite.getScale().y);

            //load col
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //load texCoords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            //load texId
            vertices[offset + 8] = texID;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * this.maxSize];
        for(int i=0;i<this.maxSize;i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        elements[offsetArrayIndex] = offset + 0;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 1;
    }
}
