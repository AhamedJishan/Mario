package Components;

import Engine.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Update() {

    }

    public Vector4f GetColor()
    {
        return this.color;
    }
}
