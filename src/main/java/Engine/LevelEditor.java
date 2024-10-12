package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditor extends Scene
{
    private GameObject obj1;
    private GameObject obj2;

    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        LoadResources();

        this.camera = new Camera(new Vector2f());

        obj1 = new GameObject("Obj1",
                new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 1);
        //obj1.AddComponent(new SpriteRenderer(new Sprite(AssetPool.GetTexture("assets/textures/blendimage1.png"))));
        obj1.AddComponent(new SpriteRenderer(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)));
        this.AddGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("Obj2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
        obj2.AddComponent(new SpriteRenderer(new Sprite(AssetPool.GetTexture("assets/textures/blendimage2.png"))));
        this.AddGameObjectToScene(obj2);
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

    @Override
    public void GUI()
    {
        ImGui.begin("Test Window");
        ImGui.text("Some random Text");
        ImGui.end();
    }
}
