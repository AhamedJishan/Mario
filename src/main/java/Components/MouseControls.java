package Components;

import Engine.GameObject;
import Engine.MouseListener;
import Engine.Window;

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
            int offset = 16;
            holdingObject.transform.position.x = MouseListener.GetOrthoX() - offset;
            holdingObject.transform.position.y = MouseListener.GetOrthoY() - offset;

            if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
                Place();
        }
    }
}
