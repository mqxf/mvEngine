package dev.mv.engine.render.draw;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.render.Display;
import dev.mv.engine.render.textures.Texture;

import java.util.ArrayList;
import java.util.List;

public class ImageBuffer {

    private List<VertexImage> buffer;
    private Display d;

    public void init(Display d) {
        this.d = d;
        buffer = new ArrayList<VertexImage>();
    }

    public void addImage(VertexImage image) {
        buffer.add(image);
    }

    public void addImage(int x1, int y1, int width1, int height1, Texture tex) {
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
		float[] texCoords = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			0.0f, 0.0f,
		};

        buffer.add(new VertexImage(indices, vertices, texCoords, tex));
    }

    public void render() throws ShaderCreateException, ShaderLinkException {

        RenderBatch batch = new RenderBatch(1000);

        buffer.clear();
    }

}
