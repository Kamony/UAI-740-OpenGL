package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args){
        DisplayManager.createDisplay();

        Loader loader = new Loader();
//        StaticShader shader = new StaticShader();
//        EntityRenderer renderer = new EntityRenderer(shader);
// cube
 float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f
        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0
        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };
        RawModel cube = loader.loadToVAO(vertices, textureCoords, new float[0], indices);
        TexturedModel texturedCube = new TexturedModel(cube, new ModelTexture(loader.loadTexture("ship")));
        Entity cubeE = new Entity(texturedCube, new Vector3f(50,3,-5),0,0,0,1f);


        RawModel model = OBJLoader.loadObjModel("ShipOBJ", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("ship"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1);

//        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-5), 0,0,0,1);
        Terrain terrain = new Terrain(0,-1,loader, new ModelTexture(loader.loadTexture("podlaha")));
        Terrain terrain2 = new Terrain(-1,-1,loader, new ModelTexture(loader.loadTexture("podlaha")));

        Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));

        Player player = new Player(texturedModel, new Vector3f(0,0,0),0,90,0,1f);
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()){
            camera.move();
            player.move();
            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
//            renderer.processEntity(entity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
//        shader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
