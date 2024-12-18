package Physics2D.Components;

import Components.Component;
import org.joml.Vector2f;

public abstract class Collider extends Component
{
    protected Vector2f offset = new Vector2f();

    public Vector2f GetOffset()
    {
        return offset;
    }
}
