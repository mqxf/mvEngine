package dev.mv.engine.render.draw;

import dev.mv.engine.render.textures.Texture;
import lombok.Getter;
import lombok.Setter;

public class VertexImage {

	@Getter @Setter
	private int[] indices;
	@Getter @Setter
	private float[] vertices;
	@Getter @Setter
	private float[] texCoords;
	@Getter @Setter
	private Texture tex;

	public VertexImage(int[] indices, float[] vertices, float[] texCoords, Texture tex) {
		this.indices = indices;
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.tex = tex;
	}
}
