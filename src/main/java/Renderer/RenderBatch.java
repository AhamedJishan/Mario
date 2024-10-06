package Renderer;

import Components.SpriteRenderer;
import Engine.Window;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch
{
    // -------------------VERTEX----------------------
    // ===============================================
    // POSITION             COLOR
    // float, float,        float, float, float, float
    // ===============================================

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize)
    {
        shader = new Shader("assets/shaders/default.glsl");
        shader.Compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices per quad
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void Start()
    {
        // Generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_STATIC_DRAW);

        // Create and upload indices buffer;
        int eboID = glGenBuffers();
        int[] indices = GenerateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the vertex attrib pointers
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
    }

    public void AddSprite(SpriteRenderer sprite)
    {
        // Get the index and add the renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        // Add properties to local vertices array
        LoadVertexProperties(index);

        if (numSprites >= maxBatchSize)
            this.hasRoom = false;
    }

    public void Render()
    {
        // For now we will rebuffer all the data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use Shader
        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().GetCamera().GetProjectionMatrix());
        shader.UploadMat4f("uView", Window.GetScene().GetCamera().GetViewMatrix());

        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        shader.Detach();
    }

    private void LoadVertexProperties(int index)
    {
        SpriteRenderer sprite = sprites[index];

        // Find offset within the array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.GetColor();

        // Add vertices with appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++)
        {
            if      (i == 1) yAdd = 0.0f;
            else if (i == 2) xAdd = 0.0f;
            else if (i == 3) yAdd = 1.0f;

            // Load position
            vertices[offset]     = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            // Load Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] GenerateIndices()
    {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++)
        {
            LoadElementIndices(elements, i);
        }
        return elements;
    }

    private void LoadElementIndices(int[] elements, int index)
    {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        // 3, 2, 0, 0, 2, 1         7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex]     = 3 + offset;
        elements[offsetArrayIndex + 1] = 2 + offset;
        elements[offsetArrayIndex + 2] = 0 + offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = 0 + offset;
        elements[offsetArrayIndex + 4] = 2 + offset;
        elements[offsetArrayIndex + 5] = 1 + offset;
    }

    public boolean HasRoom()
    {
        return  this.hasRoom;
    }

}
