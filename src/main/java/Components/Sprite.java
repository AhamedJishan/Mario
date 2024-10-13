package Components;

import Renderer.Texture;
import org.joml.Vector2f;
import org.w3c.dom.Text;

public class Sprite
{
    private Texture texture = null;
    private Vector2f[] texCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

//    public Sprite(Texture texure)
//    {
//        this.texture = texure;
//        Vector2f[] texCoords = {
//                new Vector2f(1, 1),
//                new Vector2f(1, 0),
//                new Vector2f(0, 0),
//                new Vector2f(0, 1)
//        };
//        this.texCoords = texCoords;
//    }
//
//    public Sprite(Texture texure, Vector2f[] texCoords)
//    {
//        this.texture = texure;
//        this.texCoords = texCoords;
//    }

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

}
