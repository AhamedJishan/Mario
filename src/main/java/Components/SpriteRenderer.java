package Components;

import Engine.Component;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color;
    private Sprite sprite;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite)
    {
        this.sprite = sprite;
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
        return sprite.GetTexture();
    }

    public Vector2f[] GetTexCoords()
    {
        return sprite.GetTexCoords();
    }
}