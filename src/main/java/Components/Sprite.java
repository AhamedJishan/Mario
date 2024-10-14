package Components;

import Renderer.Texture;
import org.joml.Vector2f;
import org.w3c.dom.Text;

public class Sprite
{
    private float width, height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

    public Texture GetTexture()
    {
        return this.texture;
    }

    public Vector2f[] GetTexCoords()
    {
        return this.texCoords;
    }

    public void SetTexture(Texture texture)
    {
        this.texture = texture;
    }

    public void SetTexCoords(Vector2f[] texCoords)
    {
        this.texCoords = texCoords;
    }


    public float GetWidth() {
        return width;
    }

    public void SetWidth(float width) {
        this.width = width;
    }

    public float GetHeight() {
        return height;
    }

    public void SetHeight(float height) {
        this.height = height;
    }

    public int GetTexID()
    {
        return texture == null ? -1 : texture.GetID();
    }

}
