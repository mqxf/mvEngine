#version 330

attribute vec3 vertices;

layout(location=0) in vec3 aVertPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aTexCoords;
layout(location=3) in float aTexID;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;

	gl_Position = vec4(aVertPos, 1.0);
}