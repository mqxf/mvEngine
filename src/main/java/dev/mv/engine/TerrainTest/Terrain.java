package dev.mv.engine.TerrainTest;

import dev.mv.engine.render.Display;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Terrain {
	public static int[][] generateTerrain(Display d) {
		int[][] terrainResult = new int[(int)(Math.floor(d.getWidth()/20)*Math.floor(d.getHeight()/20))][(int)(Math.floor(d.getWidth()/100)*Math.floor(d.getHeight()/100)*3)];
		int row = 0;
		int col = 0;
		double scale = 0.1;
		for(int i=0;i<(Math.ceil((d.getWidth()/20)*((d.getHeight()/20)))); i++) {
			col++;
			if(i%Math.ceil(d.getWidth()/20) == 0)row++;
			if(i%Math.ceil(d.getWidth()/20) == 0)col=0;
			if(Math.abs(Noise.noise(col*scale, (row-1)*scale, 0)) >= 0.2) {
				terrainResult[i] = new int[] {col*20, (row-1)*20, 2};
			}else{
				terrainResult[i] = new int[] {col*20, (row-1)*20, 1};
			}
			
			
		}
		
		return terrainResult;
	}
	
	static int[] loadFID(String path) {
		int[] res = null;
		StringBuilder string = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(path)));
			String line;
			while((line = reader.readLine()) != null) {
				string.append(line);
			}
			String resString = string.toString();
			resString = resString.replace(" ", "");
			res = new int[resString.length()];
			for(int i=0;i<res.length;i++) {
				res[i] = Character.getNumericValue(resString.charAt(i));;
			}
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
