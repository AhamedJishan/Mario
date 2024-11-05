package Engine;

import Components.Component;
import Editor.JImGui;
import org.joml.Vector2f;

public class Transform extends Component
{
    public Vector2f position;
    public Vector2f scale;
    public float rotation;
    public int zIndex;

    public Transform()
    {
        Init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position)
    {
        Init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale)
    {
        Init(position, scale);
    }

    public void Init(Vector2f position, Vector2f scale)
    {
        this.position = position;
        this.scale = scale;
        this.zIndex = 0;
    }

    @Override
    public void GUI()
    {
        gameObject.name = JImGui.InputText("Name: ", gameObject.name);
        JImGui.DrawVec2Control("Position: ", this.position);
        JImGui.DrawVec2Control("Scale: ", this.scale, 32.0f);
        rotation = JImGui.DragFloat("Rotation: ", this.rotation);
        zIndex = JImGui.DragInt("zIndex: ", this.zIndex);
    }

    public Transform Copy()
    {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void Copy(Transform to)
    {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform)o;
        return t.position.equals(this.position) &&
                t.scale.equals(this.scale) &&
                t.rotation == this.rotation &&
                t.zIndex == this.zIndex;
    }
}
