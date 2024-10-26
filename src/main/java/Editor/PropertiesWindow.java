package Editor;

import Components.NonPickable;
import Engine.GameObject;
import Engine.MouseListener;
import Renderer.PickingTexture;
import Scenes.Scene;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow
{
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

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
            // Only set activeGameObject when x,y is inside the game viewport
            if (x >= 0 && x <= pickingTexture.GetWidth() && y >= 0 && y <= pickingTexture.GetHeight())
            {
                GameObject pickedObj = currentScene.GetGameObject(gameObjectId);
                if (pickedObj != null && pickedObj.GetComponent(NonPickable.class) == null)
                    activeGameObject = pickedObj;
                else if (pickedObj == null && !MouseListener.IsDragging())
                    activeGameObject = null;
            }
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
