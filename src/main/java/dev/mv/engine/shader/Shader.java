package dev.mv.engine.shader;

import dev.mv.engine.exceptions.ShaderCreateException;
import dev.mv.engine.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;

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
        this.vertexCode = FileUtils.loadShaderFile(vertexShader);
        this.fragmentCode = FileUtils.loadShaderFile(fragmentShader);
    }

    private void make() throws ShaderCreateException {
        this.programID = glCreateProgram();

        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, this.vertexCode);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) != 1) {
            throw new ShaderCreateException("vertex shader error: "+glGetShaderInfoLog(vertexShader));
        }

        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, this.fragmentCode);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != 1) {
            throw new ShaderCreateException("fragment shader error: "+glGetShaderInfoLog(fragmentShader));
        }
    }

    public void use(){
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);

        glBindAttribLocation(programID,0 , "vertices");
        glLinkProgram(programID);
        if((glGetProgrami(programID, GL_LINK_STATUS)) != 1) {
            System.err.println("link program error: "+glGetProgramInfoLog(programID));
            System.exit(1);
        }
        glValidateProgram(programID);
        if((glGetProgrami(programID, GL_VALIDATE_STATUS)) != 1) {
            System.err.println("validate program error: "+glGetProgramInfoLog(programID));
            System.exit(1);
        }
    }
}
