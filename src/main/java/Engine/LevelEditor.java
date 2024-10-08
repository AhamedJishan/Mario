package Engine;

import Components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditor extends Scene
{
    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        this.camera = new Camera(new Vector2f());

        GameObject mario = new GameObject("Mario", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        mario.AddComponent(new SpriteRenderer(AssetPool.GetTexture("assets/textures/mario.png")));;
        this.AddGameObjectToScene(mario);

        GameObject goomba = new GameObject("Goomba", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        goomba.AddComponent(new SpriteRenderer(AssetPool.GetTexture("assets/textures/goomba.png")));;
        this.AddGameObjectToScene(goomba);

        LoadResources();
    }

    private void LoadResources()
    {
        AssetPool.GetShader("assets/shaders/default.glsl");
    }

    @Override
    public void Update(float dt)
    {
        for (GameObject gameObject: this.gameObjects) gameObject.Update(dt);

        this.renderer.Render();
    }
}
