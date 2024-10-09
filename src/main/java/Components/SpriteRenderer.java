package Components;

import Engine.Component;
import Engine.Transform;
import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component
{
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty= false;

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
    public void Start()
    {
        this.lastTransform = gameObject.transform.Copy();
    }

    @Override
    public void Update()
    {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.Copy(this.lastTransform);
            isDirty = true;
        }

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

    public void SetSprite (Sprite sprite)
    {
        this.sprite = sprite;
        isDirty = true;
    }

    public void SetColor(Vector4f color)
    {
        if (!this.color.equals(this.color))
        {
            this.color = color;
            this.isDirty = true;
        }
    }

    public boolean IsDirty()
    {
        return this.isDirty;
    }

    public void SetClean()
    {
        this.isDirty = false;
    }
}