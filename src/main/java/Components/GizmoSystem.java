package Components;

import Engine.KeyListener;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class GizmoSystem extends Component
{
    private SpriteSheet spriteSheet;
    private int usingGizmo = 0;

    public GizmoSystem(SpriteSheet spriteSheet)
    {
        this.spriteSheet = spriteSheet;
    }

    @Override
    public void Start()
    {
        gameObject.AddComponent(new TranslateGizmo(spriteSheet.GetSprite(1), Window.GetImGuiLayer().GetPropertiesWindow()));
        gameObject.AddComponent(new ScaleGizmo(spriteSheet.GetSprite(2), Window.GetImGuiLayer().GetPropertiesWindow()));
    }

    @Override
    public void Update(float dt)
    {
        if (usingGizmo == 0)
        {
            gameObject.GetComponent(TranslateGizmo.class).SetUsing();
            gameObject.GetComponent(ScaleGizmo.class).SetNotUsing();
        }
        else if (usingGizmo == 1)
        {
            gameObject.GetComponent(TranslateGizmo.class).SetNotUsing();
            gameObject.GetComponent(ScaleGizmo.class).SetUsing();
        }

        if (KeyListener.IsKeyPressed(GLFW_KEY_G))
            usingGizmo = 0;
        else if (KeyListener.IsKeyPressed(GLFW_KEY_S))
            usingGizmo = 1;
    }
}
