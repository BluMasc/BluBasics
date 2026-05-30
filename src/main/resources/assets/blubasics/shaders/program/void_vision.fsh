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

    vec2 texel = (1.8 / InSize);

    float tl = lum(texture(DiffuseSampler, uv + texel*vec2(-1,-1)).rgb);
    float t  = lum(texture(DiffuseSampler, uv + texel*vec2( 0,-1)).rgb);
    float tr = lum(texture(DiffuseSampler, uv + texel*vec2( 1,-1)).rgb);

    float l  = lum(texture(DiffuseSampler, uv + texel*vec2(-1, 0)).rgb);
    float r  = lum(texture(DiffuseSampler, uv + texel*vec2( 1, 0)).rgb);

    float bl = lum(texture(DiffuseSampler, uv + texel*vec2(-1, 1)).rgb);
    float b  = lum(texture(DiffuseSampler, uv + texel*vec2( 0, 1)).rgb);
    float br = lum(texture(DiffuseSampler, uv + texel*vec2( 1, 1)).rgb);

    float gx = (tl + SQRT2*l + bl) - (tr + SQRT2*r + br);
    float gy = (tl + SQRT2*t + tr) - (bl + SQRT2*b + br);

    float edge = sqrt(gx * gx + gy * gy);

    edge = pow(edge, 0.65);

    edge = smoothstep(0.08, 0.28, edge);

    edge *= 1.6;

    vec3 edgeColor = mix(
        vec3(0.25, 0.00, 0.55),
        vec3(0.85, 0.25, 1.00),
        edge
    );

    float strength = 1.2;

    float depth = texture(DepthSampler, uv).r;
    float distanceFade = 1.0 - pow(depth, 2.8);

    fragColor = vec4(edgeColor * edge * strength * distanceFade, 1.0);
}