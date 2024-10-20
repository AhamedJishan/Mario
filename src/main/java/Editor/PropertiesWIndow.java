package Editor;

import Engine.GameObject;
import Engine.MouseListener;
import Renderer.PickingTexture;
import Scenes.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWIndow
{
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture = null;

    public PropertiesWIndow(PickingTexture pickingTexture)
    {
        this.pickingTexture = pickingTexture;
    }

    public void Update(float dt, Scene currentScene)
    {
        if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            int x = (int)MouseListener.GetViewportX();
            int y = (int)MouseListener.GetViewportY();
            int gameObjectId = pickingTexture.ReadPixel(x, y);
            if (!(x < 0 || x > pickingTexture.GetWidth() || y < 0 || y > pickingTexture.GetHeight()))
                activeGameObject = currentScene.GetGameObject(gameObjectId);
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

}
