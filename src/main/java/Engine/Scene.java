package Engine;

import Components.SpriteRenderer;
import Renderer.Renderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene()
    {

    }

    public void Init()
    {
    }

    public void Start()
    {
        for (GameObject gameObject : gameObjects)
        {
            gameObject.Start();
            this.renderer.Add(gameObject);
        }
        isRunning = true;
    }

    public void AddGameObjectToScene(GameObject gameObject)
    {
        if (!isRunning)
        {
            gameObjects.add(gameObject);
        }
        else
        {
            gameObjects.add(gameObject);
            gameObject.Start();
            this.renderer.Add(gameObject);
        }
    }

    public abstract void Update(float dt);

    public Camera GetCamera()
    {
        return this.camera;
    }
}
