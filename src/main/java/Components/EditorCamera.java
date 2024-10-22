package Components;

import Engine.Camera;
import Engine.KeyListener;
import Engine.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class EditorCamera extends Component
{
    private float dragDebounce = 1.0f / 60.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;
    private float lerpTime = 0.0f;

    private boolean resetView = false;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;

    public EditorCamera(Camera levelEditorCamera)
    {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void Update(float dt)
    {
        if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0)
        {
            this.clickOrigin = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());
            dragDebounce -= dt;
            return;
        }
        else if (MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE))
        {
            Vector2f mousePos = new Vector2f(MouseListener.GetOrthoX(), MouseListener.GetOrthoY());
            Vector2f deltaPos = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.position.sub(deltaPos.mul(dt).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
            return;
        }
        if (!MouseListener.MouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce <= 0)
            dragDebounce = 1.0f / 60.0f;

        if (MouseListener.GetScrollY() != 0.0f)
        {
            float addValue = (float)Math.pow(Math.abs(MouseListener.GetScrollY() * scrollSensitivity), 1 / levelEditorCamera.GetZoom());
            addValue *= -Math.signum(MouseListener.GetScrollY());
            levelEditorCamera.AddZoom(addValue);
        }

        if (KeyListener.IsKeyPressed(GLFW_KEY_R))
            resetView = true;

        if (resetView)
        {
            levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
            this.lerpTime += 0.1f * dt;

            levelEditorCamera.SetZoom(this.levelEditorCamera.GetZoom() + ((1.0f - this.levelEditorCamera.GetZoom()) * lerpTime));

            if (Math.abs(levelEditorCamera.position.x) <= 5.0f && Math.abs(levelEditorCamera.position.y) <= 5.0f)
            {
                levelEditorCamera.position.set(0f, 0f);
                levelEditorCamera.SetZoom(1.0f);
                lerpTime = 0.0f;
                resetView = false;
            }
        }
    }
}
