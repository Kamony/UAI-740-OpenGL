package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args){
        DisplayManager.createDisplay();

        Loader loader = new Loader();
//        StaticShader shader = new StaticShader();
//        Renderer renderer = new Renderer(shader);
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

        RawModel model = OBJLoader.loadObjModel("Ship2", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("ship"));
        TexturedModel texturedModel = new TexturedModel(model, texture);
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-5), 0,0,0,1);
        Camera camera = new Camera();

        Light light = new Light(new Vector3f(0,0,-5), new Vector3f(1,1,1));

        MasterRenderer renderer = new MasterRenderer();
        while(!Display.isCloseRequested()){
            entity.increaseRotation(0f,.5f,0f);
            camera.move();
//            renderer.prepare();
//
//            shader.start();
//            shader.loadLight(light);
//            shader.loadViewMatrix(camera);
//            renderer.render(entity, shader);
//            shader.stop();
            renderer.processEntity(entity);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }
//        shader.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
