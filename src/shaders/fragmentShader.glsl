#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main(void){

    vec3 normalizedNormal = normalize(surfaceNormal);
    vec3 normalizedToLightVector = normalize(toLightVector);

    float dot = dot(normalizedNormal, normalizedToLightVector);
    float brightness = max(dot, 0.2);

    vec3 diffuse = brightness * lightColour;

    vec3 normalizedToCameraVector = normalize(toCameraVector);
    vec3 lightDirection = -normalizedToLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, normalizedNormal);

    float specularLightFactor = dot(reflectedLightDirection, normalizedToCameraVector);
    specularLightFactor = max(specularLightFactor, 0.0);
    float dampedFactor = pow(specularLightFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

    out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 1.0);
    out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}