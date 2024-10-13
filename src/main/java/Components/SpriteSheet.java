package Components;

import Renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing) {
        sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.GetHeight() - spriteHeight;
        for (int i = 0; i < numSprites; i++)
        {
            float topY = (currentY + spriteHeight) / (float)texture.GetHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.GetWidth();
            float leftX = currentX / (float)texture.GetWidth();
            float bottomY = currentY / (float)texture.GetHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.SetTexture(this.texture);
            sprite.SetTexCoords(texCoords);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX >= this.texture.GetWidth())
            {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }

        }
    }

    public Sprite GetSprite(int index)
    {
        return this.sprites.get(index);
    }

}
