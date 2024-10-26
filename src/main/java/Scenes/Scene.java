package Scenes;

import Components.Component;
import Components.ComponentDeserializer;
import Engine.Camera;
import Engine.GameObject;
import Engine.GameObjectDeserializer;
import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelLoaded = false;

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

    public GameObject GetGameObject(int gameObjectId)
    {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.GetUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public abstract void Update(float dt);
    public abstract void Render();

    public Camera GetCamera()
    {
        return this.camera;
    }

    public void GUI()
    {

    }

    public void SaveExit()
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
            this.levelLoaded = true;
        }
    }
}
