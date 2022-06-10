#vertex
#version 330

attribute vec3 vertices;

void main(){
	gl_Position = vec4(vertices, 1.0);
}

#fragment
