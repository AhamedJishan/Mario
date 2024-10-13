package Engine;

import Components.SpriteRenderer;
import Renderer.Renderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene
{
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
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

    public abstract void Update(float dt);

    public Camera GetCamera()
    {
        return this.camera;
    }

    public void SceneGUI()
    {
        if (activeGameObject != null)
        {
            ImGui.begin(activeGameObject.name + ": Inspector");
            activeGameObject.GUI();
            ImGui.end();
        }

        GUI();
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
            writer.write(gson.toJson(this.gameObjects));
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
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++)
            {
                AddGameObjectToScene(objs[i]);
            }
            this.levelLoaded = true;
        }
    }
}
