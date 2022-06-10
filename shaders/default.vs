#version 330

attribute vec3 vertices;

layout(location=0) in vec3 aVertPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aTexCoords;

out vec4 fColor;
out vec2 fTexCoords;

void main(){
    fColor = aColor;
    fTexCoords = aTexCoords;

	gl_Position = vec4(vertices, 1.0);
}