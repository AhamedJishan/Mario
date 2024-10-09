package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditor extends Scene
{
    private GameObject mario;
    private SpriteSheet spriteSheet;

    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        LoadResources();

        this.camera = new Camera(new Vector2f());

        spriteSheet = AssetPool.GetSpriteSheet("assets/textures/spritesheet.png");

        mario = new GameObject("Mario", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)));
        mario.AddComponent(new SpriteRenderer(spriteSheet.GetSprite(0)));
        this.AddGameObjectToScene(mario);

        GameObject goomba = new GameObject("Goomba", new Transform(new Vector2f(700, 100), new Vector2f(256, 256)));
        goomba.AddComponent(new SpriteRenderer(spriteSheet.GetSprite(15)));
        this.AddGameObjectToScene(goomba);
    }

    private void LoadResources()
    {
        AssetPool.GetShader("assets/shaders/default.glsl");

        AssetPool.AddSpriteSheet("assets/textures/spritesheet.png",
                new SpriteSheet(AssetPool.GetTexture("assets/textures/spritesheet.png"),
                        16, 16, 26, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void Update(float dt)
    {
        spriteFlipTimeLeft -= dt;
        if (spriteFlipTimeLeft <= 0.0f)
        {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex >= 4)
                spriteIndex = 1;

            mario.GetComponent(SpriteRenderer.class).SetSprite(spriteSheet.GetSprite(spriteIndex));
        }

        for (GameObject gameObject: this.gameObjects) gameObject.Update(dt);

        this.renderer.Render();
    }
}
