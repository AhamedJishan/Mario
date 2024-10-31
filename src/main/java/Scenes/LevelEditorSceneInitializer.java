package Scenes;

import Components.*;
import Engine.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import util.AssetPool;

import static util.Settings.GRID_HEIGHT;
import static util.Settings.GRID_WIDTH;

public class LevelEditorSceneInitializer extends SceneInitializer
{
    private SpriteSheet sprites;

    private GameObject levelManager;

    public LevelEditorSceneInitializer()
    {
    }

    @Override
    public void Init(Scene scene)
    {
        sprites = AssetPool.GetSpriteSheet("assets/textures/spritesheets/decorationsAndBlocks.png");
        SpriteSheet gizmos = AssetPool.GetSpriteSheet("assets/textures/gizmos.png");

        levelManager  = new GameObject("LevelEditorManager");
        levelManager.SetNoSerialize();
        levelManager.AddComponent(new MouseControls());
        levelManager.AddComponent(new GridLines());
        levelManager.AddComponent(new EditorCamera(scene.GetCamera()));
        levelManager.AddComponent(new GizmoSystem(gizmos));
        scene.AddGameObjectToScene(levelManager);
    }

    @Override
    public void LoadResources(Scene scene)
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

        for (GameObject gameObject : scene.GetGameObjects())
        {
            SpriteRenderer spriteRenderer = gameObject.GetComponent(SpriteRenderer.class);
            if (spriteRenderer != null)
            {
                if (spriteRenderer.GetTexture() != null)
                    spriteRenderer.SetTexture(AssetPool.GetTexture(spriteRenderer.GetTexture().GetFilepath()));
            }
        }
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
                GameObject object = Prefabs.GenerateSpriteObject(sprite, GRID_WIDTH, GRID_HEIGHT);
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
