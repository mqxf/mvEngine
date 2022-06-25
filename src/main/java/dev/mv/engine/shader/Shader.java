package dev.mv.engine.shader;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.exceptions.ShaderLinkException;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private String vertexCode;
    private String fragmentCode;
    @Getter @Setter
    private int vertexShader;
    @Getter @Setter
    private int fragmentShader;
    @Getter @Setter
    private int programID;

    public Shader(String vertexShader, String fragmentShader) {
        this.vertexCode = "";
        this.fragmentCode = "";
        Scanner v = new Scanner(this.getClass().getResourceAsStream(vertexShader));
        Scanner f = new Scanner(this.getClass().getResourceAsStream(fragmentShader));
        try {
            while (v.hasNext()) {
                this.vertexCode += v.nextLine() + "\n";
            }
            while (f.hasNext()) {
                this.fragmentCode += f.nextLine() + "\n";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void make() throws ShaderCreateException {
        this.programID = glCreateProgram();

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, this.vertexCode);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1) {
            throw new ShaderCreateException("vertex shader error: " + glGetShaderInfoLog(vertexShader));
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, this.fragmentCode);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1) {
            throw new ShaderCreateException("fragment shader error: " + glGetShaderInfoLog(fragmentShader));
        }
    }

    public void use() throws ShaderLinkException {
        glAttachShader(this.programID, vertexShader);
        glAttachShader(this.programID, fragmentShader);

        glBindAttribLocation(this.programID,0 , "vertices");
        glLinkProgram(this.programID);
        if((glGetProgrami(this.programID, GL_LINK_STATUS)) != 1) {
            throw new ShaderLinkException("link program error: " + glGetProgramInfoLog(this.programID));
        }
        glValidateProgram(this.programID);
        if((glGetProgrami(this.programID, GL_VALIDATE_STATUS)) != 1) {
            throw new ShaderLinkException("link program error: " + glGetProgramInfoLog(this.programID));
        }

        glUseProgram(this.programID);
    }

    public int getProgramID() {
        return this.programID;
    }

    public void setUniform1f(String name, float value) {
        int location = glGetUniformLocation(this.programID, name);
        if(location != -1) {
            glUniform1f(location, value);
        }
    }

    public void setUniform1i(String name, int value) {
        int location = glGetUniformLocation(this.programID, name);
        if(location != -1) {
            glUniform1f(location, value);
        }
    }

    public void setUniform1iv(String name, int[] value) {
        int location = glGetUniformLocation(this.programID, name);
        if(location != -1) {
            glUniform1iv(location, value);
        }
    }
}
