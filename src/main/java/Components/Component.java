package Components;

import Editor.JImGui;
import Engine.GameObject;
import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component
{
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public transient GameObject gameObject = null;

    public void Start()
    {

    }

    public void Update(float dt)
    {

    }

    public void EditorUpdate(float dt)
    {

    }

    public void GUI()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient)
                    continue;

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate)
                    field.setAccessible(true);

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class)
                {
                    int val = (int)value;
                    field.set(this, JImGui.DragInt(name, val));
                }
                else if (type == float.class)
                {
                    float val = (float)value;
                    field.set(this, JImGui.DragFloat(name, val));
                }
                else if (type == boolean.class)
                {
                    boolean val = (boolean)value;
                    boolean imFloat = val;
                    if (ImGui.checkbox(name + ": ", val))
                        field.set(this, !val);
                }
                else if (type == Vector2f.class)
                {
                    Vector2f val = (Vector2f)value;
                    JImGui.DrawVec2Control(name, val);
                }
                else if (type == Vector3f.class)
                {
                    Vector3f val = (Vector3f)value;
                    JImGui.DrawVec3Control(name, val);
                }
                else if (type == Vector4f.class)
                {
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                }
                else if (type.isEnum())
                {
                    String[] enumValues = GetEnumValues(type);
                    String enumType = ((Enum)value).name();
                    ImInt index = new ImInt(IndexOf(enumType, enumValues));
                    if (ImGui.combo(field.getName(), index, enumValues, enumValues.length))
                        field.set(this, type.getEnumConstants()[index.get()]);
                }

                if (isPrivate)
                    field.setAccessible(false);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void GenerateID()
    {
        if (this.uid == -1)
            this.uid = ID_COUNTER++;
    }

    public int GetUid()
    {
        return this.uid;
    }

    private <T extends Enum<T>> String[] GetEnumValues(Class<T> enumType)
    {
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntValue : enumType.getEnumConstants())
        {
            enumValues[i] = enumIntValue.name();
            i++;
        }
        return enumValues;
    }

    private int IndexOf(String str, String[] arr)
    {
        for (int i =0; i < arr.length; i++)
            if (arr[i].equals(str))
                return i;

        return -1;
    }

    public void Destroy()
    {

    }

    public static void Init(int maxId)
    {
        ID_COUNTER = maxId;
    }
}
