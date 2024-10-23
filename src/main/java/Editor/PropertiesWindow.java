package Editor;

import Engine.GameObject;
import Engine.MouseListener;
import Renderer.PickingTexture;
import Scenes.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow
{
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture = null;

    private float debounce = 0f;

    public PropertiesWindow(PickingTexture pickingTexture)
    {
        this.pickingTexture = pickingTexture;
    }

    public void Update(float dt, Scene currentScene)
    {
        debounce -= dt;
        if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0)
        {
            int x = (int)MouseListener.GetViewportX();
            int y = (int)MouseListener.GetViewportY();
            int gameObjectId = pickingTexture.ReadPixel(x, y);
            if (!(x < 0 || x > pickingTexture.GetWidth() || y < 0 || y > pickingTexture.GetHeight()))
                activeGameObject = currentScene.GetGameObject(gameObjectId);

            debounce = 0.2f;
        }
    }

    public void GUI()
    {
        if (activeGameObject != null)
        {
            ImGui.begin("Properties");
            activeGameObject.GUI();
            ImGui.end();
        }
    }

    public GameObject GetActiveGameObject()
    {
        return this.activeGameObject;
    }

}
