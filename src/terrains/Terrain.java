package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;
import utils.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 600;

    // heightmap props
    private static final float MAX_HEIGHT = 40;
    private static final float MAX_PIXEL_COLOUR = 256*256*256;


    private float x;
    private float z;
    private RawModel model;
    private ModelTexture texture;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture, String heightMap){
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float)heights.length - 1);
        int gridX = (int) Math.floor(terrainX/gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ/gridSquareSize);
        if(gridX >=heights.length -1 || gridZ >= heights.length -1 || gridX<0 || gridZ<0){
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize)/ gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize)/ gridSquareSize;
        float terrainHeight;
        if (xCoord <= (1-zCoord)) {
			terrainHeight = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			terrainHeight = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return terrainHeight;
    }

    private RawModel generateTerrain(Loader loader, String heightMap){

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/"+ heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        heights = new float [VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j,i,image);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private float getHeight(int x, int z, BufferedImage image){
        if (x<0 || x>=image.getHeight() || z< 0 || z>=image.getHeight()){
            return 0;
        }

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOUR/2f;
        height /= MAX_PIXEL_COLOUR/2f;
        height *= MAX_HEIGHT;

        return height;
    }
}
