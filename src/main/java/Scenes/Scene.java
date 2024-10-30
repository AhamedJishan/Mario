package Scenes;

import Components.Component;
import Components.ComponentDeserializer;
import Engine.Camera;
import Engine.GameObject;
import Engine.GameObjectDeserializer;
import Engine.Transform;
import Physics2D.Physics2D;
import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene
{
    private Renderer renderer = new Renderer();
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;

    private SceneInitializer sceneInitializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer sceneInitializer)
    {
        this.sceneInitializer = sceneInitializer;
        physics2D = new Physics2D();
        renderer = new Renderer();
        gameObjects = new ArrayList<>();
        isRunning = false;
    }

    public void Destroy()
    {
        for (int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).Destroy();
        }
    }

    public void Init()
    {
        this.camera = new Camera(new Vector2f());
        sceneInitializer.LoadResources(this);
        sceneInitializer.Init(this);
    }

    public void Start()
    {
        for (int i = 0; i < gameObjects.size(); i++)
        {
            gameObjects.get(i).Start();
            this.renderer.Add(gameObjects.get(i));
            physics2D.Add(gameObjects.get(i));
        }
        isRunning = true;
    }

    public GameObject CreateGameObject(String name)
    {
        GameObject gameObject = new GameObject(name);
        gameObject.AddComponent(new Transform());
        gameObject.transform = gameObject.GetComponent(Transform.class);
        return gameObject;
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
            physics2D.Add(gameObject);
        }
    }

    public List<GameObject> GetGameObjects()
    {
        return this.gameObjects;
    }

    public GameObject GetGameObject(int gameObjectId)
    {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.GetUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public void EditorUpdate(float dt)
    {
        camera.AdjustProjection();

        for (int i = 0; i < gameObjects.size(); i++)
        {
            GameObject gameObject = gameObjects.get(i);
            gameObject.EditorUpdate(dt);

            if (gameObject.IsDead())
            {
                gameObjects.remove(gameObject);
                renderer.DestroyGameObject(gameObject);
                physics2D.DestroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void Update(float dt)
    {
        camera.AdjustProjection();
        physics2D.Update(dt);

        for (int i = 0; i < gameObjects.size(); i++)
        {
            GameObject gameObject = gameObjects.get(i);
            gameObject.Update(dt);

            if (gameObject.IsDead())
            {
                gameObjects.remove(gameObject);
                renderer.DestroyGameObject(gameObject);
                physics2D.DestroyGameObject(gameObject);
                i--;
            }
        }
    }
    public void Render()
    {
        this.renderer.Render();
    }

    public Camera GetCamera()
    {
        return this.camera;
    }

    public void GUI()
    {
        this.sceneInitializer.GUI();
    }

    public void Save()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try
        {
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects)
                if (obj.DoSerialization())
                    objsToSerialize.add(obj);

            // NOTE: temporary workaround
            if (objsToSerialize.size()>0)
                writer.write(gson.toJson(objsToSerialize));
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Load()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (!inFile.equals(""))
        {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++)
            {
                AddGameObjectToScene(objs[i]);

                for (Component c : objs[i].GetAllComponents())
                {
                    if (c.GetUid() > maxCompId)
                        maxCompId = c.GetUid();
                }

                if (objs[i].GetUid() > maxGoId)
                    maxGoId = objs[i].GetUid();
            }

            maxGoId++;
            maxCompId++;
            GameObject.Init(maxGoId);
            Component.Init(maxCompId);
        }
    }
}
