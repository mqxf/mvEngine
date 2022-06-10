package dev.mv.engine.render.textures;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
    private int id;
    private int width;
    private int height;
    int[] pixels;

    public Texture(String filename) {
        if(filename != null) {
            BufferedImage img;
            try {
                img = ImageIO.read(new File(filename));
                this.width = img.getWidth();
                this.height = img.getHeight();

                this.pixels = new int[this.width * this.height];
                this.pixels = img.getRGB(0, 0, this.width, this.height, null, 0, this.width);

                ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(this.width * this.height * 4);

                for(int i=0;i<this.width*this.height;i++) {
                    int pixel = pixels[i];
                    pixelBuffer.put((byte)((pixel >> 16) & 0xFF)); //r
                    pixelBuffer.put((byte)((pixel >> 8) & 0xFF));  //g
                    pixelBuffer.put((byte)(pixel & 0xFF));		   //b
                    pixelBuffer.put((byte)((pixel >> 24) & 0xFF)); //a
                }
                pixelBuffer.flip();
                this.id = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, this.id);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}

