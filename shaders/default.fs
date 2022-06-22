#version 330

uniform sampler2D TEX_SAMPLER;

in vec3 fColor;
in vec2 fTexCoords;

void main(){
    gl_FragColor = vec4(fColor, 1.0);
}