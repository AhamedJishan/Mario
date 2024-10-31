package Physics2D.Components;

import Renderer.DebugDraw;
import org.joml.Vector2f;
import org.joml.Vector3f;

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

    @Override
    public void EditorUpdate(float dt)
    {
        Vector2f center = new Vector2f(gameObject.transform.position).add(offset);
        Vector2f dimensions = new Vector2f(halfSize.x * 2, halfSize.y * 2);
        DebugDraw.AddBox2D(center, dimensions, gameObject.transform.rotation);
    }
}
