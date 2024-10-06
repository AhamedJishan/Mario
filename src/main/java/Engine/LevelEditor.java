package Engine;

import Components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditor extends Scene
{
    // For fps calculations
    private float timeElapsed;
    private int frames;

    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f());

        // TEST CODE for batch rendering ==============================================
        int xOffset = 10;
        int yOffset = 10;
        float totalWidth = (float) (600 - 2 * xOffset);
        float totalHeight = (float) (300 - 2 * yOffset);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++)
        {
            for (int y = 0; y < 100; y++)
            {
                float xPos = xOffset + x * sizeX;
                float yPos = yOffset + y * sizeY;

                GameObject gameObject = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                gameObject.AddComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/totalHeight, 1, 1)));
                this.AddGameObjectToScene(gameObject);
            }
        }
        // ============================================================================
    }

    @Override
    public void Update(float dt)
    {
        // For FPS
        timeElapsed += dt;
        frames++;
        if (timeElapsed >= 1)
        {
            System.out.println("FPS: " + (frames));
            frames = 0;
            timeElapsed = 0.0f;
        }

        for (GameObject gameObject: this.gameObjects) gameObject.Update(dt);

        this.renderer.Render();
    }
}
