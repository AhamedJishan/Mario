package Components;

import Engine.GameObject;
import Engine.MouseListener;
import Engine.Window;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component
{
    GameObject holdingObject = null;

    public void PickupObject(GameObject gameObject)
    {
        this.holdingObject = gameObject;
        Window.GetScene().AddGameObjectToScene(gameObject);
    }

    public void Place()
    {
        this.holdingObject = null;
    }

    @Override
    public void Update(float dt)
    {
        if (this.holdingObject != null)
        {
            holdingObject.transform.position.x = MouseListener.GetOrthoX();
            holdingObject.transform.position.y = MouseListener.GetOrthoY();
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x/ Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y/ Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
                Place();
        }
    }
}
