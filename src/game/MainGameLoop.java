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
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args){
        DisplayManager.createDisplay();

        Loader loader = new Loader();
//        StaticShader shader = new StaticShader();
//        EntityRenderer renderer = new EntityRenderer(shader);
// cube
// float[] vertices = {
//                -0.5f,0.5f,-0.5f,
//                -0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,0.5f,-0.5f,
//
//                -0.5f,0.5f,0.5f,
//                -0.5f,-0.5f,0.5f,
//                0.5f,-0.5f,0.5f,
//                0.5f,0.5f,0.5f,
//
//                0.5f,0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,0.5f,
//                0.5f,0.5f,0.5f,
//
//                -0.5f,0.5f,-0.5f,
//                -0.5f,-0.5f,-0.5f,
//                -0.5f,-0.5f,0.5f,
//                -0.5f,0.5f,0.5f,
//
//                -0.5f,0.5f,0.5f,
//                -0.5f,0.5f,-0.5f,
//                0.5f,0.5f,-0.5f,
//                0.5f,0.5f,0.5f,
//
//                -0.5f,-0.5f,0.5f,
//                -0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,-0.5f,
//                0.5f,-0.5f,0.5f
//        };
//
//        float[] textureCoords = {
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0,
//                0,0,
//                0,1,
//                1,1,
//                1,0
//        };
//
//        int[] indices = {
//                0,1,3,
//                3,1,2,
//                4,5,7,
//                7,5,6,
//                8,9,11,
//                11,9,10,
//                12,13,15,
//                15,13,14,
//                16,17,19,
//                19,17,18,
//                20,21,23,
//                23,21,22
//
//        };

        RawModel earth_raw = OBJLoader.loadObjModel("Earth", loader);
        TexturedModel earth_model = new TexturedModel(earth_raw, new ModelTexture(loader.loadTexture("Earth_T")));
        TexturedModel sun_model = new TexturedModel(earth_raw, new ModelTexture(loader.loadTexture("sun")));
        Entity earth = new Entity(earth_model, new Vector3f(400,70,-400), 0,0,0,3);
        Entity sun = new Entity(sun_model, new Vector3f(400,70,-420), 0,0,0,3);

        RawModel model = OBJLoader.loadObjModel("ShipOBJ", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("ship"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1);

        Terrain terrain = new Terrain(0,-1,loader, new ModelTexture(loader.loadTexture("podlaha")),
                "podlaha_h");

        Light light = new Light(new Vector3f(400,50,-400), new Vector3f(1,1,1));

        Player player = new Player(texturedModel, new Vector3f(400,50,-400),0,90,0,1f);
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader);

        while(!Display.isCloseRequested()){
            earth.increaseRotation(0,0.1f,0);
            camera.move();
            player.move(terrain);
            renderer.processEntity(player);
            renderer.processEntity(earth);
            renderer.processEntity(sun);
            renderer.processTerrain(terrain);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
