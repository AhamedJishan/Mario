package Renderer;

import Components.Sprite;
import Components.SpriteRenderer;
import Engine.GameObject;
import Engine.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>
{
    // ---------------------------------------VERTEX---------------------------------------------
    // ==========================================================================================
    // POSITION         COLOR                           TEXCOORDS           TEX_ID      ENTITY_ID
    // float, float,    float, float, float, float      float, float        float       float
    // ==========================================================================================
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE + ENTITY_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private List<Texture> textures = new ArrayList<>();
    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;

    private Renderer renderer;

    public RenderBatch(int maxBatchSize, int zIndex, Renderer renderer)
    {
        this.renderer = renderer;
        this.zIndex = zIndex;
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
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(4);
        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
    }

    public void AddSprite(SpriteRenderer sprite)
    {
        // Get the index and add the renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        if (sprite.GetTexture() != null)
        {
            if (!textures.contains(sprite.GetTexture()))
                textures.add(sprite.GetTexture());
        }

        // Add properties to local vertices array
        LoadVertexProperties(index);

        if (numSprites >= maxBatchSize)
            this.hasRoom = false;
    }

    public void Render()
    {
        boolean rebufferData = false;
        for (int i = 0; i < numSprites; i++)
        {
            SpriteRenderer spr = sprites[i];
            if (spr.IsDirty())
            {
                LoadVertexProperties(i);
                spr.SetClean();
                rebufferData = true;
            }

            // TODO: get better solution for this
            if (spr.gameObject.transform.zIndex != zIndex)
            {
                DestroyIfExists(spr.gameObject);
                renderer.Add(spr.gameObject);
                i--;
            }
        }

        if (rebufferData)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use Shader
        Shader shader = Renderer.GetBoundShader();
        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().GetCamera().GetProjectionMatrix());
        shader.UploadMat4f("uView", Window.GetScene().GetCamera().GetViewMatrix());
        for (int i = 0; i < textures.size(); i++)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).Bind();
        }
        shader.UploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        for (int i = 0; i < textures.size(); i++)
            textures.get(i).Unbind();

        shader.Detach();
    }

    private void LoadVertexProperties(int index)
    {
        SpriteRenderer sprite = sprites[index];

        // Find offset within the array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.GetColor();
        Vector2f[] texCoords = sprite.GetTexCoords();

        int texID = 0;
        if (sprite.GetTexture() != null) {
            for (int i = 0; i < textures.size(); i++)
            {
                if (textures.get(i).equals(sprite.GetTexture()))
                {
                    texID = i + 1;
                    break;
                }
            }
        }

        boolean isRotated = sprite.gameObject.transform.rotation != 0.0f;
        Matrix4f transformMat = new Matrix4f().identity();
        if (isRotated)
        {
            transformMat.translate(sprite.gameObject.transform.position.x, sprite.gameObject.transform.position.y, 0);
            transformMat.rotate(Math.toRadians(sprite.gameObject.transform.rotation), 0,0,1);
            transformMat.scale(sprite.gameObject.transform.scale.x, sprite.gameObject.transform.scale.y, 1.0f);
        }

        // Add vertices with appropriate properties
        float xAdd = 0.5f;
        float yAdd = 0.5f;
        for (int i = 0; i < 4; i++)
        {
            if      (i == 1) yAdd = -0.5f;
            else if (i == 2) xAdd = -0.5f;
            else if (i == 3) yAdd = 0.5f;

            // Rotate if necessary
            Vector4f currentPos = new Vector4f(sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x),
                    sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y),
                    0, 1);
            if (isRotated)
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMat);

            // Load position
            vertices[offset]     = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            // Load Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load Tex Coords
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            // Load TexID
            vertices[offset + 8] = texID;

            // Load Entity id
            vertices[offset + 9] = sprite.gameObject.GetUid() + 1;

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

    public boolean DestroyIfExists(GameObject gameObject)
    {
        SpriteRenderer spriteRenderer = gameObject.GetComponent(SpriteRenderer.class);

        for (int i = 0; i < numSprites; i++)
        {
            if (sprites[i] == spriteRenderer)
            {
                for (int j = i; j < numSprites - 1; j++)
                {
                    sprites[j] = sprites[j+1];
                    sprites[j].SetDirty();
                }
                numSprites--;
                return true;
            }
        }
        return false;
    }

    public boolean HasRoom()
    {
        return  this.hasRoom;
    }

    public boolean HasTextureRoom()
    {
        return this.textures.size() < 8;
    }

    public boolean HasTexture(Texture tex)
    {
        return this.textures.contains(tex);
    }

    public int ZIndex()
    {
        return this.zIndex;
    }

    // For sort()
    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.ZIndex());
    }
}
