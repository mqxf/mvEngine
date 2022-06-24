#version 330

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;

uniform sampler2D TEX_SAMPLER[8];

void main(){
    if(false) gl_FragColor = vec4(vec4(fColor));
    else gl_FragColor = texture(TEX_SAMPLER[int(fTexID)], fTexCoords);
}