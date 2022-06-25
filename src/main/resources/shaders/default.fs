#version 330

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;

uniform sampler2D TEX_SAMPLER[8];

void main(){
    if(fTexID > 0){
        vec4 c = texture(TEX_SAMPLER[int(fTexID)], fTexCoords);
        gl_FragColor = vec4(c.x + 0.0, c.y + 0.0, c.z + 0.0, c.w - 0.0);
    }
    else gl_FragColor = vec4(vec4(fColor));
}