package Renderer;

import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class DebugDraw
{
    private static int MAX_LINES = 500;

    private static List<Line2D> lines = new ArrayList<>();
    // 6 floats per vertex, and 2 vertex per line
    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.GetShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;
    // TODO: TEMPORARY WORKAROUND
    private static boolean lineListEdited = false;

    public static void Start()
    {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glLineWidth(2.0f);
    }

    public static void BeginFrame()
    {
        if (!started)
        {
            Start();
            started = true;
        }

        // Remove deadlines
        for (int i = 0; i < lines.size(); i++)
        {
            if (lines.get(i).BeginFrame() < 0)
            {
                lines.remove(i);
                lineListEdited = true;
                --i;
            }
        }
    }

    public static void Draw()
    {
        if (lines.size() <= 0) return;
        if (lineListEdited)
            Arrays.fill(vertexArray, 0, vertexArray.length, 0.0f);

        int index = 0;
        for (Line2D line : lines)
        {
            for (int i = 0; i < 2; i++)
            {
                Vector2f position = i == 0 ? line.GetFrom() : line.GetTo();
                Vector3f color = line.GetColor();

                // Load Position
                vertexArray[index]      = position.x;
                vertexArray[index + 1]  = position.y;
                vertexArray[index + 2]  = -10.0f;

                // Load Color
                vertexArray[index + 3]  = color.x;
                vertexArray[index + 4]  = color.y;
                vertexArray[index + 5]  = color.z;

                index+=6;
            }
        }

        // Update buffer at the GPU
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        if (lineListEdited)
        {
            lineListEdited = false;
            glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);
        }
        float[] arr = Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2);
        glBufferSubData(GL_ARRAY_BUFFER, 0, arr);

        shader.Use();
        shader.UploadMat4f("uProjection", Window.GetScene().GetCamera().GetProjectionMatrix());
        shader.UploadMat4f("uView", Window.GetScene().GetCamera().GetViewMatrix());

        glBindVertexArray(vaoID);
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);
        glBindVertexArray(0);

        shader.Detach();
    }

    // ========================================================
    // Add Line2D methods
    // ========================================================
    public static void AddLine2D(Vector2f from, Vector2f to)
    {
        AddLine2D(from, to, new Vector3f(1.0f, 0.0f, 0.0f), 1);
    }

    public static void AddLine2D(Vector2f from, Vector2f to, Vector3f color)
    {
        AddLine2D(from, to, color, 1);
    }

    public static void AddLine2D(Vector2f from, Vector2f to, Vector3f color, int lifeTime)
    {
        if (lines.size() >= MAX_LINES) return;
        DebugDraw.lines.add(new Line2D(from, to, color, lifeTime));
    }
}
