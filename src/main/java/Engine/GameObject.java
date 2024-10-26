package Engine;

import Components.Component;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    private boolean doSerialization = true;

    public GameObject(String name, Transform transform, int zIndex)
    {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;

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
        //for (Component component : components) component.Update(dt);
        for (int i = 0; i < components.size(); i++)
            components.get(i).Update(dt);
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
            c.GUI();
    }

    public int ZIndex()
    {
        return this.zIndex;
    }

    public static void Init(int maxID)
    {
        ID_COUNTER = maxID;
    }

    public int GetUid()
    {
        return this.uid;
    }

    public List<Component> GetAllComponents()
    {
        return this.components;
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
