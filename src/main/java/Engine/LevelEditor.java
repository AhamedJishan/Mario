package Engine;

import Components.FontRenderer;
import Components.SpriteRenderer;
import Renderer.Shader;
import Renderer.Texture;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditor extends Scene
{
    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            // POSITION                 // COLOR                    // TEX COORDS
             200.0f,   10.0f,  0.0f,    1.0f, 0.0f, 0.0f, 1.0f,     1.0f, 0.0f,     // Bottom right
              10.0f,  200.0f,  0.0f,    0.0f, 1.0f, 0.0f, 1.0f,     0.0f, 1.0f,     // Top left
             200.0f,  200.0f,  0.0f,    0.0f, 0.0f, 1.0f, 1.0f,     1.0f, 1.0f,     // Top right
              10.0f,   10.0f,  0.0f,    0.0f, 1.0f, 1.0f, 1.0f,     0.0f, 0.0f      // Bottom left
    };

    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    private GameObject testObj;

    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        System.out.println("Creating 'TestObject'");
        testObj = new GameObject("Test Object");
        testObj.AddComponent(new SpriteRenderer());
        testObj.AddComponent(new FontRenderer());
        this.AddGameObjectToScene(this.testObj);

        camera = new Camera(new Vector2f());

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.Compile();
        testTexture = new Texture("assets/textures/awesomeface.png");

        // ==========================================================
        // Generate VAO, VBOm and EBO buffer objects, and send to GPU
        // ==========================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create an int buffer of indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        // create VBO and upload the vertex buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorsSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorsSize + uvSize) * Float.BYTES;

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorsSize) * Float.BYTES);

        glBindVertexArray(0);
    }

    @Override
    public void Update(float dt)
    {
        // Activate the Shader
        defaultShader.Use();

        // Upload texture to the shader
        defaultShader.UploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.Bind();

        defaultShader.UploadMat4f("uProjection", camera.GetProjectionMatrix());
        defaultShader.UploadMat4f("uView", camera.GetViewMatrix());

        // Bind the VAO we'll be using
        glBindVertexArray(vaoID);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        defaultShader.Detach();


        for (GameObject gameObject: this.gameObjects) gameObject.Update(dt);
    }
}
