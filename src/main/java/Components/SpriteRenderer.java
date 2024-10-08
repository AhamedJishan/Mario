package Components;

import Engine.Component;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color;
    private Vector2f[] texCoords;
    private Texture texture;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.texture = null;
    }

    public SpriteRenderer(Texture texture)
    {
        this.texture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
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

    public Texture GetTexture()
    {
        return this.texture;
    }

    public Vector2f[] GetTexCoords()
    {
        Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        return texCoords;
    }
}
