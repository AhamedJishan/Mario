package Engine;

import org.joml.Vector2f;

public class Transform
{
    public Vector2f position;
    public Vector2f scale;

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
        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
