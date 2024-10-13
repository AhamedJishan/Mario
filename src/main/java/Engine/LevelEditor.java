package Engine;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.SpriteSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.SetColor(new Vector4f(0.3f, 0.3f, 0.6f, 0.7f));
        obj1.AddComponent(obj1SpriteRenderer);
        this.AddGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("Obj2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.SetTexture(AssetPool.GetTexture("assets/textures/blendimage2.png"));
        obj2SpriteRenderer.SetSprite(obj2Sprite);
        obj2.AddComponent(obj2SpriteRenderer);
        this.AddGameObjectToScene(obj2);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(obj1));
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
