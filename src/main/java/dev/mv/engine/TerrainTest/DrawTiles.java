package dev.mv.engine.TerrainTest;

import dev.mv.engine.render.draw.Draw;
import dev.mv.engine.render.textures.Texture;

public class DrawTiles {
	
	static Texture tile_grass = new Texture("/textures/grass.png");
	static Texture tile_stone = new Texture("/textures/stone.png");
	
	public static void draw(int[][] terrainData) {
		for(int i=0;i<terrainData.length;i++) {
			if(terrainData[i][2] == 1) {
				Draw.drawImage(terrainData[i][0], terrainData[i][1], 20, 20, 0, tile_grass);
			}
			if(terrainData[i][2] == 2) {
				Draw.drawImage(terrainData[i][0], terrainData[i][1], 20, 20, 0, tile_stone);
			}
		}
	}
}
