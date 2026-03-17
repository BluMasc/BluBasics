#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform vec2 InSize;

out vec4 fragColor;

const float SQRT2 = 1.41421356237;

float lum(vec3 c) {
    return dot(c, vec3(0.299,0.587,0.114));
}

void main() {
    vec2 uv = gl_FragCoord.xy / InSize;
    vec2 texel = 1.0 / InSize;

    float tl = lum(texture(DiffuseSampler, uv + texel*vec2(-1,-1)).rgb);
    float t  = lum(texture(DiffuseSampler, uv + texel*vec2(0,-1)).rgb);
    float tr = lum(texture(DiffuseSampler, uv + texel*vec2(1,-1)).rgb);
    float l  = lum(texture(DiffuseSampler, uv + texel*vec2(-1,0)).rgb);
    float r  = lum(texture(DiffuseSampler, uv + texel*vec2(1,0)).rgb);
    float bl = lum(texture(DiffuseSampler, uv + texel*vec2(-1,1)).rgb);
    float b  = lum(texture(DiffuseSampler, uv + texel*vec2(0,1)).rgb);
    float br = lum(texture(DiffuseSampler, uv + texel*vec2(1,1)).rgb);

    float gx = (tl + SQRT2*l + bl) - (tr + SQRT2*r + br);
    float gy = (tl + SQRT2*t + tr) - (bl + SQRT2*b + br);

    float edge = sqrt(gx*gx + gy*gy);
    edge = step(0.4, edge);
    edge = smoothstep(0.05,0.25, edge);

    vec3 edgeColor = mix(vec3(0.1,0.0,0.3), vec3(0.3,0.1,0.5), edge);
    float strength = 0.5;

    float depth = texture(DepthSampler, uv).r;
    float distanceFade = 1.0 - pow(depth, 1.8); // tweak 0.8 as needed

    fragColor = vec4(edgeColor * edge * strength * distanceFade, 1.0);
}
