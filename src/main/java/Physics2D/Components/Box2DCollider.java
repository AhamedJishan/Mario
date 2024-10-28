package Physics2D.Components;

import Components.Component;
import org.joml.Vector2f;

public class Box2DCollider extends Component
{
    private Vector2f halfSize = new Vector2f(1);

    public Vector2f GetHalfSize() {
        return halfSize;
    }

    public void SetHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }
}
