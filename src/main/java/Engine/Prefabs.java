package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject GenerateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = new GameObject("Sprite_Obj_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.SetSprite(sprite);
        block.AddComponent(spriteRenderer);

        return block;
    }
}
