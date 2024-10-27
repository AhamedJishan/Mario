package Scenes;

import Components.*;
import Engine.*;
import Renderer.DebugDraw;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.AssetPool;

public class LevelEditor extends Scene
{
    private GameObject obj1;
    private GameObject obj2;
    private SpriteSheet sprites;

    GameObject levelManager = new GameObject("LevelEditorManager");

    public LevelEditor()
    {
    }

    @Override
    public void Init()
    {
        LoadResources();
        sprites = AssetPool.GetSpriteSheet("assets/textures/spritesheets/decorationsAndBlocks.png");
        SpriteSheet gizmos = AssetPool.GetSpriteSheet("assets/textures/gizmos.png");

        this.camera = new Camera(new Vector2f());
        levelManager.AddComponent(new MouseControls());
        levelManager.AddComponent(new GridLines());
        levelManager.AddComponent(new EditorCamera(this.camera));
        levelManager.AddComponent(new GizmoSystem(gizmos));

        levelManager.Start();

//        obj1 = new GameObject("Obj1",
//                new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 1);
//        SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
//        obj1SpriteRenderer.SetColor(new Vector4f(0.3f, 0.3f, 0.6f, 0.7f));
//        obj1.AddComponent(obj1SpriteRenderer);
//        obj1.AddComponent(new Rigidbody());
//        this.AddGameObjectToScene(obj1);
//
//        obj2 = new GameObject("Obj2",
//                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
//        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
//        Sprite obj2Sprite = new Sprite();
//        obj2Sprite.SetTexture(AssetPool.GetTexture("assets/textures/blendimage2.png"));
//        obj2SpriteRenderer.SetSprite(obj2Sprite);
//        obj2.AddComponent(obj2SpriteRenderer);
//        this.AddGameObjectToScene(obj2);
    }

    private void LoadResources()
    {
        AssetPool.GetShader("assets/shaders/default.glsl");

        // TODO: FIX TEXTURE SAVE SYSTEM TO USE PATH INSTEAD OF ID
        AssetPool.GetTexture("assets/textures/blendimage2.png");
        AssetPool.AddSpriteSheet("assets/textures/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.GetTexture("assets/textures/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.AddSpriteSheet("assets/textures/gizmos.png",
                new SpriteSheet(AssetPool.GetTexture("assets/textures/gizmos.png"),
                        24, 48, 3, 0));

        for (GameObject gameObject : gameObjects)
        {
            SpriteRenderer spriteRenderer = gameObject.GetComponent(SpriteRenderer.class);
            if (spriteRenderer != null)
            {
                if (spriteRenderer.GetTexture() != null)
                    spriteRenderer.SetTexture(AssetPool.GetTexture(spriteRenderer.GetTexture().GetFilepath()));
            }
        }
    }


    float t = 0.0f;
    @Override
    public void Update(float dt)
    {
        camera.AdjustProjection();
        levelManager.Update(dt);
        t += 50 * dt;
        DebugDraw.AddBox2D(new Vector2f(32*15, 32*10), new Vector2f(64, 32), t);
        DebugDraw.AddCircle2D(new Vector2f(32*20, 32*10), 64, new Vector3f(1,0,0));

        for (GameObject gameObject: this.gameObjects) gameObject.Update(dt);
    }

    @Override
    public void Render()
    {
        this.renderer.Render();
    }

    @Override
    public void GUI()
    {
        ImGui.begin("Level Editor Stuff");
        levelManager.GUI();
        ImGui.end();

        ImGui.begin("Test Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.Size(); i++)
        {
            Sprite sprite = sprites.GetSprite(i);
            float width = sprite.GetWidth() * 3;
            float height = sprite.GetHeight() * 3;
            int id = sprite.GetTexID();
            Vector2f[] texCoords = sprite.GetTexCoords();

            ImGui.pushID(i);
            // Getting texCoords[0] and [2] as 0 is topRight and 2 bottom left
            if (ImGui.imageButton(id, width, height, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
            {
                GameObject object = Prefabs.GenerateSpriteObject(sprite, 32, 32);
                // Attach this gameobject to the mouse cursor
                levelManager.GetComponent(MouseControls.class).PickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + width;
            if (i + 1 < sprites.Size() && nextButtonX2 < windowX2)
                ImGui.sameLine();

        }

        ImGui.end();
    }
}
