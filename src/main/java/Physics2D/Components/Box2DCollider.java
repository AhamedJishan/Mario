package Physics2D.Components;

import org.joml.Vector2f;

public class Box2DCollider extends Collider
{
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    public Vector2f GetHalfSize() {
        return halfSize;
    }

    public void SetHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f GetOrigin()
    {
        return origin;
    }
}
