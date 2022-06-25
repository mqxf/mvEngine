package dev.mv.engine.render;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import dev.mv.engine.render.draw.Draw;
import dev.mv.engine.render.draw.RenderBatch;
import dev.mv.engine.render.handler.DisplayManager;
import dev.mv.engine.render.handler.Handle;
import dev.mv.engine.render.handler.Time;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display{
    private String title;
    private int width, height;
    private boolean rez;
    private float red = 0.0f;

    public static long winAddr;

    private int vertexShader;
    private int fragmentShader;

    private DisplayManager handle;
    private RenderBatch batch;

    private float[] vertices = {
            1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
    };

    private int[] indices = {
            0, 1, 2, 1, 3, 2
    };

    public Display(String title, int width, int height, boolean rez, DisplayManager handle) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.rez = rez;
        this.handle = handle;
        Handle.setDisplay(this);
    }

    public void run() throws IOException, ShaderCreateException, ShaderLinkException {
        init();
        Draw.init(this);
        batch = Draw.getBatch();
        handle.start();
        loop();

        glfwFreeCallbacks(winAddr);
        glfwDestroyWindow(winAddr);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Err: Cannot init glfw!");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, rez ? GL_TRUE : GL_FALSE);

        winAddr = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (winAddr == NULL) {
            throw new IllegalStateException("Err: Window cannot be created!");
        }

        glfwMakeContextCurrent(winAddr);
        GL.createCapabilities();
        glfwSwapInterval(1);
    }

    private void loop() {
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,  GL_ONE_MINUS_SRC_ALPHA);

        long last = System.currentTimeMillis();
        long now = 0;
        long diff = 0;
        double delta = 0;
        int fps = 0;
        int maxFps = 60;

        while (!glfwWindowShouldClose(winAddr)) {
            now = System.currentTimeMillis();
            diff = now - last;

            if (diff >= Math.ceil(1000.0f / (float) maxFps)) {
                glfwPollEvents();
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                delta = (float) diff / 1000.0f;
                fps = (int) (1000 / diff);
                last = now;
                Time.setDelta(delta);
                Time.setFps(fps);
                System.out.println("FPS: " + fps + ", delta: " + delta);
                handle.update();

                batch.pushBatchToGPU();
                batch.render();

                glDisable(GL_TEXTURE_2D);
                glfwSwapBuffers(winAddr);
            }
        }
    }

    public static String readFile(String path, String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(path + filename)));
            String line;
            while((line = reader.readLine()) != null) {
                string.append(line);
                string.append("\n");
            }
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        return string.toString();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public long getAddr() {
        return this.winAddr;
    }
}
