#version 330

uniform sampler2D TEX_SAMPLER;

in vec4 fColor;
in vec2 fTexCoords;

void main(){
    gl_FragColor = texture(TEX_SAMPLER, fTexCoords);
}