package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import utils.Maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static final float RED = 0.5f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.5f;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer() {
        // optimalizace vykreslovani
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        this.projectionMatrix = Maths.createProjectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
        this.entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(Light light, Camera camera){
        prepare();
        shader.loadSkyColour(RED,GREEN,BLUE);
        // entity
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();

        // teren
        terrainShader.start();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch!=null){
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED,GREEN,BLUE,1);
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }
}
