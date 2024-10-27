package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject GenerateSpriteObject(Sprite sprite, float sizeX, float sizeY)
    {
        GameObject block = Window.GetScene().CreateGameObject("Sprite_Obj_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.SetSprite(sprite);
        block.AddComponent(spriteRenderer);

        return block;
    }
}
