package Engine;

import Components.Component;
import Components.ComponentDeserializer;
import Components.SpriteRenderer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;

    private boolean doSerialization = true;
    private boolean isDead = false;

    public GameObject(String name)
    {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T GetComponent(Class<T> componentClass)
    {
        for (Component c : components)
        {
            if (componentClass.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return componentClass.cast(c);
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                    assert false: "Error: Casting Component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void RemoveComponent(Class<T> componentClass)
    {
        for (int i = 0; i<components.size(); i++)
        {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass()))
            {
                components.remove(i);
                return;
            }
        }
    }

    public void AddComponent(Component c)
    {
        c.GenerateID();
        this.components.add(c);
        c.gameObject = this;
    }

    public void Update(float dt)
    {
        for (int i = 0; i < components.size(); i++)
            components.get(i).Update(dt);
    }

    public void EditorUpdate(float dt)
    {
        for (int i = 0; i < components.size(); i++)
            components.get(i).EditorUpdate(dt);
    }

    public void Start()
    {
        //for (Component component : components) component.Start();
        for (int i = 0; i < components.size(); i++)
            components.get(i).Start();
    }

    public void GUI()
    {
        for (Component c : components)
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.GUI();
    }

    public void Destroy()
    {
        this.isDead = true;
        for (int i = 0; i < components.size(); i++)
        {
            components.get(i).Destroy();
        }
    }

    public static void Init(int maxID)
    {
        ID_COUNTER = maxID;
    }

    public GameObject Copy()
    {
        // TODO: Come up with a better solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);
        obj.GenerateUID();
        for (Component component: obj.GetAllComponents())
            component.GenerateID();

        SpriteRenderer spriteRenderer = obj.GetComponent(SpriteRenderer.class);
        if (spriteRenderer != null && spriteRenderer.GetTexture() != null)
        {
            spriteRenderer.SetTexture(AssetPool.GetTexture(spriteRenderer.GetTexture().GetFilepath()));
        }

        return obj;
    }

    public int GetUid()
    {
        return this.uid;
    }

    public boolean IsDead()
    {
        return this.isDead;
    }

    public List<Component> GetAllComponents()
    {
        return this.components;
    }

    public void GenerateUID()
    {
        this.uid = ID_COUNTER++;
    }

    public void SetNoSerialize()
    {
        this.doSerialization = false;
    }

    public boolean DoSerialization()
    {
        return this.doSerialization;
    }

}
