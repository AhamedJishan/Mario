package Engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
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
        for (GameObject gameObject : gameObjects) gameObject.Start();
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
        }
    }

    public abstract void Update(float dt);
}
